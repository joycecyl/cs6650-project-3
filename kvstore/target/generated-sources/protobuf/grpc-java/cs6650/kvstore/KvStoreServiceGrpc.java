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
public final class KvStoreServiceGrpc {

  private KvStoreServiceGrpc() {}

  public static final String SERVICE_NAME = "cs6650.kvstore.KvStoreService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_PUT =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.KvStoreService", "put"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_GET =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.KvStoreService", "get"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_DELETE =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.KvStoreService", "delete"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_PREPARE =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.KvStoreService", "prepare"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_COMMIT =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.KvStoreService", "commit"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
      cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> METHOD_ABORT =
      io.grpc.MethodDescriptor.<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage, cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "cs6650.kvstore.KvStoreService", "abort"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              cs6650.kvstore.KvStoreServiceOuterClass.KvMessage.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static KvStoreServiceStub newStub(io.grpc.Channel channel) {
    return new KvStoreServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static KvStoreServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new KvStoreServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static KvStoreServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new KvStoreServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class KvStoreServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void put(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PUT, responseObserver);
    }

    /**
     */
    public void get(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET, responseObserver);
    }

    /**
     */
    public void delete(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE, responseObserver);
    }

    /**
     * <pre>
     * two pc rpc
     * </pre>
     */
    public void prepare(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_PREPARE, responseObserver);
    }

    /**
     */
    public void commit(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_COMMIT, responseObserver);
    }

    /**
     */
    public void abort(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ABORT, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_PUT,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_PUT)))
          .addMethod(
            METHOD_GET,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_GET)))
          .addMethod(
            METHOD_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_DELETE)))
          .addMethod(
            METHOD_PREPARE,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_PREPARE)))
          .addMethod(
            METHOD_COMMIT,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_COMMIT)))
          .addMethod(
            METHOD_ABORT,
            asyncUnaryCall(
              new MethodHandlers<
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage,
                cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>(
                  this, METHODID_ABORT)))
          .build();
    }
  }

  /**
   */
  public static final class KvStoreServiceStub extends io.grpc.stub.AbstractStub<KvStoreServiceStub> {
    private KvStoreServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KvStoreServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KvStoreServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KvStoreServiceStub(channel, callOptions);
    }

    /**
     */
    public void put(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PUT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void get(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delete(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * two pc rpc
     * </pre>
     */
    public void prepare(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_PREPARE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void commit(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_COMMIT, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void abort(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request,
        io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ABORT, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class KvStoreServiceBlockingStub extends io.grpc.stub.AbstractStub<KvStoreServiceBlockingStub> {
    private KvStoreServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KvStoreServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KvStoreServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KvStoreServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage put(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PUT, getCallOptions(), request);
    }

    /**
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage get(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET, getCallOptions(), request);
    }

    /**
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage delete(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE, getCallOptions(), request);
    }

    /**
     * <pre>
     * two pc rpc
     * </pre>
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage prepare(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_PREPARE, getCallOptions(), request);
    }

    /**
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage commit(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_COMMIT, getCallOptions(), request);
    }

    /**
     */
    public cs6650.kvstore.KvStoreServiceOuterClass.KvMessage abort(cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ABORT, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class KvStoreServiceFutureStub extends io.grpc.stub.AbstractStub<KvStoreServiceFutureStub> {
    private KvStoreServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private KvStoreServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected KvStoreServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new KvStoreServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> put(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PUT, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> get(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> delete(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request);
    }

    /**
     * <pre>
     * two pc rpc
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> prepare(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_PREPARE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> commit(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_COMMIT, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage> abort(
        cs6650.kvstore.KvStoreServiceOuterClass.KvMessage request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ABORT, getCallOptions()), request);
    }
  }

  private static final int METHODID_PUT = 0;
  private static final int METHODID_GET = 1;
  private static final int METHODID_DELETE = 2;
  private static final int METHODID_PREPARE = 3;
  private static final int METHODID_COMMIT = 4;
  private static final int METHODID_ABORT = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final KvStoreServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(KvStoreServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PUT:
          serviceImpl.put((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
              (io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>) responseObserver);
          break;
        case METHODID_GET:
          serviceImpl.get((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
              (io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
              (io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>) responseObserver);
          break;
        case METHODID_PREPARE:
          serviceImpl.prepare((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
              (io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>) responseObserver);
          break;
        case METHODID_COMMIT:
          serviceImpl.commit((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
              (io.grpc.stub.StreamObserver<cs6650.kvstore.KvStoreServiceOuterClass.KvMessage>) responseObserver);
          break;
        case METHODID_ABORT:
          serviceImpl.abort((cs6650.kvstore.KvStoreServiceOuterClass.KvMessage) request,
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

  private static final class KvStoreServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return cs6650.kvstore.KvStoreServiceOuterClass.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (KvStoreServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new KvStoreServiceDescriptorSupplier())
              .addMethod(METHOD_PUT)
              .addMethod(METHOD_GET)
              .addMethod(METHOD_DELETE)
              .addMethod(METHOD_PREPARE)
              .addMethod(METHOD_COMMIT)
              .addMethod(METHOD_ABORT)
              .build();
        }
      }
    }
    return result;
  }
}
