package org.neb.fluxin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.neb.fluxin.contract.TestMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/test")
@Tag(name = "Test APIs")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TestController {

    KafkaTemplate<String, org.neb.fluxin.avro.TestMessage> kafkaTemplate;

    @Operation(summary = "Create JD", description = "API to Create new job description",
            security = @SecurityRequirement(name = "basic_bearer"))
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "common/no-content", description = "No content", content = @Content()),
//        @ApiResponse(responseCode = "common/not-found", description = "Not Found",
//                content = @Content(schema = @Schema(implementation = TestMessage.class))),
//        @ApiResponse(responseCode = "400", description = "Bad Request",
//                content = @Content(schema = @Schema(implementation = String.class)))
//    })
    @ResponseStatus(OK)
    @PostMapping
    public ResponseEntity<String> test(@RequestBody @Valid TestMessage request) {
        try {
            ProducerRecord<String, org.neb.fluxin.avro.TestMessage> record = new ProducerRecord<>(
                    "test", null,
                    UUID.randomUUID().toString(), new org.neb.fluxin.avro.TestMessage(request.content()),
                    List.of(
                            new RecordHeader("type", "message".getBytes(StandardCharsets.UTF_8))
                    ));
            kafkaTemplate.send(record);

            return ResponseEntity.ok("Test successful with request: " + request);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error creating job: " + e.getMessage());
        }
    }

}
