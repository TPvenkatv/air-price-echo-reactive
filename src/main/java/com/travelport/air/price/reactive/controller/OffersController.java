package com.travelport.air.price.reactive.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelport.air.offerresource.model.OfferQueryBuildFromProducts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
@RequestMapping("${openapi.offerResource.base-path:/11/air/price}")
@Slf4j
public class OffersController {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebClient webClient;

    @PostMapping(value = "/offers/buildfromproducts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<OfferQueryBuildFromProducts>> buildFromProducts(
            @RequestBody OfferQueryBuildFromProducts offerQueryBuildFromProducts,
            @RequestHeader(value = "TraceId", required = false) String traceId,
            @RequestHeader(value = "XAUTH_TRAVELPORT_ACCESSGROUP", required = false) String XAUTH_TRAVELPORT_ACCESSGROUP,
            @RequestHeader(value = "TravelportPlusSessionID", required = false) String travelportPlusSessionID) throws Exception {

        log.info("Request: " + om.writeValueAsString(offerQueryBuildFromProducts));
        log.info("Header XAUTH_TRAVELPORT_ACCESSGROUP: " + XAUTH_TRAVELPORT_ACCESSGROUP);
        log.info("Header TravelportPlusSessionID: " + travelportPlusSessionID);
        log.info("Header TraceId: " + traceId);

        return simulateExternalCall(offerQueryBuildFromProducts, traceId, XAUTH_TRAVELPORT_ACCESSGROUP, travelportPlusSessionID);
    }

    private Mono<ResponseEntity<OfferQueryBuildFromProducts>> simulateExternalCall(OfferQueryBuildFromProducts offerQueryBuildFromProducts,
                                                                             String traceId,
                                                                             String XAUTH_TRAVELPORT_ACCESSGROUP,
                                                                             String travelportPlusSessionID) throws Exception{
        Mono<OfferQueryBuildFromProducts> offerQueryBuildFromProductsResp = webClient.post()
                .uri("/11/air/price/offers/buildfromproducts")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(om.writeValueAsString(offerQueryBuildFromProducts)), String.class)
                .retrieve()
                .bodyToMono(OfferQueryBuildFromProducts.class).log();
        Mono<ResponseEntity<OfferQueryBuildFromProducts>> responseEntity =offerQueryBuildFromProductsResp.map(
                offerQueryBuildFromProductsObj -> {
                    ResponseEntity<OfferQueryBuildFromProducts> body = ResponseEntity.status(HttpStatus.OK)
                            .header("TraceId", traceId)
                            .header("XAUTH_TRAVELPORT_ACCESSGROUP", XAUTH_TRAVELPORT_ACCESSGROUP)
                            .header("TravelportPlusSessionID", travelportPlusSessionID)
                            .body(offerQueryBuildFromProductsObj);
                    return body;
                });

//        Mono<ResponseEntity<OfferQueryBuildFromProducts>> responseEntity = ResponseEntity.status(HttpStatus.OK)
//                .header("TraceId", traceId)
//                .header("XAUTH_TRAVELPORT_ACCESSGROUP", XAUTH_TRAVELPORT_ACCESSGROUP)
//                .header("TravelportPlusSessionID", travelportPlusSessionID)
//                .body(offerQueryBuildFromProductsResp);
        return responseEntity;
    }

//    private Mono<StudentDto> convert(Mono<OfferQueryBuildFromProducts> offerQueryBuildFromProductsResp) {
//        return offerQueryBuildFromProductsResp.map(
//                offerQueryBuildFromProducts -> {
//                    ResponseEntity.status(HttpStatus.OK)
//                            .header("TraceId", traceId)
//                            .header("XAUTH_TRAVELPORT_ACCESSGROUP", XAUTH_TRAVELPORT_ACCESSGROUP)
//                            .header("TravelportPlusSessionID", travelportPlusSessionID)
//                            .body(offerQueryBuildFromProductsResp);
//                }
//        );
//    }
}
