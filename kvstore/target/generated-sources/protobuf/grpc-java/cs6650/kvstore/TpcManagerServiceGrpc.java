package cs6650.kvstore;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: KvStoreService.proto")
public final class TpcManagerServiceGrpc {

  private TpcManagerServiceGrpc() {}

  public static final String SERVICE_NAME = "cs6650.kvstore.TpcManagerService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_START_TPC =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.TpcManagerService", "startTpc"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TpcManagerServiceStub newStub(io.grpc.Channel channel) {
    return new TpcManagerServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TpcManagerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TpcManagerServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TpcManagerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TpcManagerServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TpcManagerServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void startTpc(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_START_TPC, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_START_TPC,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_START_TPC)))
          .build();
    }
  }

  /**
   */
  public static final class TpcManagerServiceStub extends io.grpc.stub.AbstractStub<TpcManagerServiceStub> {
    private TpcManagerServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TpcManagerServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TpcManagerServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TpcManagerServiceStub(channel, callOptions);
    }

    /**
     */
    public void startTpc(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_START_TPC, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TpcManagerServiceBlockingStub extends io.grpc.stub.AbstractStub<TpcManagerServiceBlockingStub> {
    private TpcManagerServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TpcManagerServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TpcManagerServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TpcManagerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage startTpc(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_START_TPC, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TpcManagerServiceFutureStub extends io.grpc.stub.AbstractStub<TpcManagerServiceFutureStub> {
    private TpcManagerServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TpcManagerServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TpcManagerServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TpcManagerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> startTpc(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_START_TPC, getCallOptions()), request);
    }
  }

  private static final int METHODID_START_TPC = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TpcManagerServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TpcManagerServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START_TPC:
          serviceImpl.startTpc((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
              (io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class TpcManagerServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cs6650.kvstore.KvStoreServiceOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TpcManagerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TpcManagerServiceDescriptorSupplier())
              .addMethod(METHOD_START_TPC)
              .build();
        }
      }
    }
    return result;
  }
}
