package com.example.family;

import family.MessageId;
import family.StorageServiceGrpc;
import family.StoreResult;
import family.StoredMessage;
import io.grpc.stub.StreamObserver;

public class StorageServiceImpl extends StorageServiceGrpc.StorageServiceImplBase {

    private final DiskManager diskManager;

    public StorageServiceImpl(DiskManager diskManager) {
        this.diskManager = diskManager;
    }

    @Override
    public void store(StoredMessage request, StreamObserver<StoreResult> responseObserver) {
        int id = request.getId();
        String text = request.getText();

        System.out.println("💾 gRPC Store İsteği Geldi: ID=" + id);

        boolean isSuccess = diskManager.saveMessage(id, text);

        StoreResult result = StoreResult.newBuilder()
                .setSuccess(isSuccess)
                .build();

        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void retrieve(family.MessageId request, io.grpc.stub.StreamObserver<family.StoredMessage> responseObserver) {
        // DiskManager kullanarak dosyayı yükle
        String content = diskManager.loadMessage(request.getId());

        if (content == null) {
            content = "NOT_FOUND"; // Boş dönmek yerine belirteç dön
        }

        family.StoredMessage response = family.StoredMessage.newBuilder()
                .setId(request.getId())
                .setText(content)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}