package com.pm.billing_service.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> responseObserver) {
        log.info("createBillingAccount request received: {}", billingRequest.toString());
        
        // Business logic - e.g save to database, perform calculates etc
    
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("generated-account-id-123")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
