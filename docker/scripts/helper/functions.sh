#!/bin/bash

update_cli_permissions() {
  # Script to get Confluent CLI `curl -k CLI_URL -o confluent`
  # Update the confluent cli permissions
  CONFLUENT_CLI='../kafka/bin/confluent'
  if [ -f $CONFLUENT_CLI ]; then
    echo "Updating permission of ${CONFLUENT_CLI} to 744"
    chmod 744 $CONFLUENT_CLI
  fi
}

create_client_files() {
  echo "Creating client files"
  cat ../templates/superuser.template | envsubst > ../kafka/configs/superuser.properties
  cat ../templates/client.template | envsubst > ../kafka/configs/client.properties
}

create_env_file() {
    # Xóa nội dung file trước khi ghi
    : > ../.env

#    echo DOCKER_REGISTRY=$DOCKER_REGISTRY > .env
#    echo CONFLUENT=$CONFLUENT >> .env
#    echo CONFLUENT_DOCKER_TAG=$CONFLUENT_DOCKER_TAG >> .env
#    echo CONFLUENT_SHORT=$CONFLUENT_SHORT >> .env
#    echo CONFLUENT_PREVIOUS=$CONFLUENT_PREVIOUS >> .env
#    echo CONFLUENT_RELEASE_TAG_OR_BRANCH=$CONFLUENT_RELEASE_TAG_OR_BRANCH >> .env
#    echo CONFLUENT_MAJOR=$CONFLUENT_MAJOR >> .env
#    echo CONFLUENT_MINOR=$CONFLUENT_MINOR >> .env
#    echo CONFLUENT_PATCH=$CONFLUENT_PATCH >> .env
#    echo CP_VERSION_FULL=$CP_VERSION_FULL >> .env

    echo POSTGRES_USER=$POSTGRES_USER >> ../.env
    echo POSTGRES_PASSWORD=$POSTGRES_PASSWORD >> ../.env

    echo KC_BOOTSTRAP_ADMIN_USERNAME=$KC_BOOTSTRAP_ADMIN_USERNAME >> ../.env
    echo KC_BOOTSTRAP_ADMIN_PASSWORD=$KC_BOOTSTRAP_ADMIN_PASSWORD >> ../.env

    echo IDP_TOKEN_ENDPOINT=$IDP_TOKEN_ENDPOINT >> ../.env
    echo IDP_JWKS_ENDPOINT=$IDP_JWKS_ENDPOINT >> ../.env
    echo IDP_EXPECTED_ISSUER=$IDP_EXPECTED_ISSUER >> ../.env
    echo IDP_AUTH_ENDPOINT=$IDP_AUTH_ENDPOINT >> ../.env
    echo IDP_DEVICE_AUTH_ENDPOINT=$IDP_DEVICE_AUTH_ENDPOINT >> ../.env
    echo SUB_CLAIM_NAME=$SUB_CLAIM_NAME >> ../.env
    echo GROUP_CLAIM_NAME=$GROUP_CLAIM_NAME >> ../.env
    echo EXPECTED_AUDIENCE=$EXPECTED_AUDIENCE >> ../.env

    # Client configurations
    echo APP_GROUP_NAME=$APP_GROUP_NAME >> ../.env

    echo SUPERUSER_CLIENT_ID=$SUPERUSER_CLIENT_ID >> ../.env
    echo SUPERUSER_CLIENT_SECRET=$SUPERUSER_CLIENT_SECRET >> ../.env

    echo SR_CLIENT_ID=$SR_CLIENT_ID >> ../.env
    echo SR_CLIENT_SECRET=$SR_CLIENT_SECRET >> ../.env

    echo RP_CLIENT_ID=$RP_CLIENT_ID >> ../.env
    echo RP_CLIENT_SECRET=$RP_CLIENT_SECRET >> ../.env

    echo CONNECT_CLIENT_ID=$CONNECT_CLIENT_ID >> ../.env
    echo CONNECT_CLIENT_SECRET=$CONNECT_CLIENT_SECRET >> ../.env

    echo CONNECT_SECRET_PROTECTION_CLIENT_ID=$CONNECT_SECRET_PROTECTION_CLIENT_ID >> ../.env
    echo CONNECT_SECRET_PROTECTION_CLIENT_SECRET=$CONNECT_SECRET_PROTECTION_CLIENT_SECRET >> ../.env

    echo C3_CLIENT_ID=$C3_CLIENT_ID >> ../.env
    echo C3_CLIENT_SECRET=$C3_CLIENT_SECRET >> ../.env

    echo CLIENT_APP_ID=$CLIENT_APP_ID >> ../.env
    echo CLIENT_APP_SECRET=$CLIENT_APP_SECRET >> ../.env

    echo SSO_CLIENT_ID=$SSO_CLIENT_ID >> ../.env
    echo SSO_CLIENT_SECRET=$SSO_CLIENT_SECRET >> ../.env

    echo SSO_SUPER_USER_GROUP=$SSO_SUPER_USER_GROUP >> ../.env
    echo SSO_USER_GROUP=$SSO_USER_GROUP >> ../.env

    echo KSQL_CLIENT_ID=$KSQL_CLIENT_ID >> ../.env
    echo KSQL_CLIENT_SECRET=$KSQL_CLIENT_SECRET >> ../.env
}

assign_group_owner() {
  group_name="$1"

  # Lấy access token từ IDP
  auth_token=$(curl -s -d "client_id=$SUPERUSER_CLIENT_ID" \
                    -d "client_secret=$SUPERUSER_CLIENT_SECRET" \
                    -d "grant_type=client_credentials" \
                    "$IDP_TOKEN_ENDPOINT" | \
               grep -Po '"access_token": *\K"[^"]*"' | grep -o '[^"]*')

  echo "Access Token: $auth_token"

  MDS_RBAC_ENDPOINT=http://broker1:8091/security/1.0/principals

  # Gán role ResourceOwner cho group
  curl -X POST "$MDS_RBAC_ENDPOINT/User:$CLIENT_APP_ID/roles/ResourceOwner/bindings" \
    -H "Authorization: Bearer $auth_token" \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    -d "{
      \"scope\": {
        \"clusters\": {
          \"kafka-cluster\": \"vHCgQyIrRHG8Jv27qI2h3Q\"
        }
      },
      \"resourcePatterns\": [
        {
          \"resourceType\": \"Group\",
          \"name\": \"$group_name\",
          \"patternType\": \"LITERAL\"
        }
      ]
    }"
}

