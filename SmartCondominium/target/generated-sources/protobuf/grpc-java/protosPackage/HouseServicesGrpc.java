package protosPackage;

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
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: HouseServices.proto")
public final class HouseServicesGrpc {

  private HouseServicesGrpc() {}

  public static final String SERVICE_NAME = "protosPackage.HouseServices";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.House,
      protosPackage.HouseServicesOuterClass.HouseResponse> METHOD_HELLO =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.House, protosPackage.HouseServicesOuterClass.HouseResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "hello"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.House.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.HouseResponse.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("hello"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.Statistic,
      protosPackage.HouseServicesOuterClass.Response> METHOD_ADD_STATISTIC =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.Statistic, protosPackage.HouseServicesOuterClass.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "addStatistic"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.Statistic.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.Response.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("addStatistic"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.exitHouse,
      protosPackage.HouseServicesOuterClass.Response> METHOD_EXIT =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.exitHouse, protosPackage.HouseServicesOuterClass.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "exit"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.exitHouse.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.Response.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("exit"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.House,
      protosPackage.HouseServicesOuterClass.Response> METHOD_ELECTION =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.House, protosPackage.HouseServicesOuterClass.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "election"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.House.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.Response.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("election"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.House,
      protosPackage.HouseServicesOuterClass.Response> METHOD_NEW_COORDINATOR =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.House, protosPackage.HouseServicesOuterClass.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "newCoordinator"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.House.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.Response.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("newCoordinator"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.BoostRequest,
      protosPackage.HouseServicesOuterClass.BoostResponse> METHOD_REQUEST_BOOST =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.BoostRequest, protosPackage.HouseServicesOuterClass.BoostResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "requestBoost"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.BoostRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.BoostResponse.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("requestBoost"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.HouseServicesOuterClass.House,
      protosPackage.HouseServicesOuterClass.Response> METHOD_REPLY =
      io.grpc.MethodDescriptor.<protosPackage.HouseServicesOuterClass.House, protosPackage.HouseServicesOuterClass.Response>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.HouseServices", "reply"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.House.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.HouseServicesOuterClass.Response.getDefaultInstance()))
          .setSchemaDescriptor(new HouseServicesMethodDescriptorSupplier("reply"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HouseServicesStub newStub(io.grpc.Channel channel) {
    return new HouseServicesStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HouseServicesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new HouseServicesBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HouseServicesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new HouseServicesFutureStub(channel);
  }

  /**
   */
  public static abstract class HouseServicesImplBase implements io.grpc.BindableService {

    /**
     */
    public void hello(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.HouseResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_HELLO, responseObserver);
    }

    /**
     */
    public void addStatistic(protosPackage.HouseServicesOuterClass.Statistic request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD_STATISTIC, responseObserver);
    }

    /**
     */
    public void exit(protosPackage.HouseServicesOuterClass.exitHouse request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_EXIT, responseObserver);
    }

    /**
     * <pre>
     *election
     * </pre>
     */
    public void election(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ELECTION, responseObserver);
    }

    /**
     */
    public void newCoordinator(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_NEW_COORDINATOR, responseObserver);
    }

    /**
     * <pre>
     *extra usage
     * </pre>
     */
    public void requestBoost(protosPackage.HouseServicesOuterClass.BoostRequest request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.BoostResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REQUEST_BOOST, responseObserver);
    }

    /**
     */
    public void reply(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_REPLY, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_HELLO,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.House,
                protosPackage.HouseServicesOuterClass.HouseResponse>(
                  this, METHODID_HELLO)))
          .addMethod(
            METHOD_ADD_STATISTIC,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.Statistic,
                protosPackage.HouseServicesOuterClass.Response>(
                  this, METHODID_ADD_STATISTIC)))
          .addMethod(
            METHOD_EXIT,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.exitHouse,
                protosPackage.HouseServicesOuterClass.Response>(
                  this, METHODID_EXIT)))
          .addMethod(
            METHOD_ELECTION,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.House,
                protosPackage.HouseServicesOuterClass.Response>(
                  this, METHODID_ELECTION)))
          .addMethod(
            METHOD_NEW_COORDINATOR,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.House,
                protosPackage.HouseServicesOuterClass.Response>(
                  this, METHODID_NEW_COORDINATOR)))
          .addMethod(
            METHOD_REQUEST_BOOST,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.BoostRequest,
                protosPackage.HouseServicesOuterClass.BoostResponse>(
                  this, METHODID_REQUEST_BOOST)))
          .addMethod(
            METHOD_REPLY,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.HouseServicesOuterClass.House,
                protosPackage.HouseServicesOuterClass.Response>(
                  this, METHODID_REPLY)))
          .build();
    }
  }

  /**
   */
  public static final class HouseServicesStub extends io.grpc.stub.AbstractStub<HouseServicesStub> {
    private HouseServicesStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HouseServicesStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HouseServicesStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HouseServicesStub(channel, callOptions);
    }

    /**
     */
    public void hello(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.HouseResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_HELLO, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void addStatistic(protosPackage.HouseServicesOuterClass.Statistic request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD_STATISTIC, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void exit(protosPackage.HouseServicesOuterClass.exitHouse request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_EXIT, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *election
     * </pre>
     */
    public void election(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ELECTION, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void newCoordinator(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_NEW_COORDINATOR, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     *extra usage
     * </pre>
     */
    public void requestBoost(protosPackage.HouseServicesOuterClass.BoostRequest request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.BoostResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REQUEST_BOOST, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reply(protosPackage.HouseServicesOuterClass.House request,
        io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_REPLY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HouseServicesBlockingStub extends io.grpc.stub.AbstractStub<HouseServicesBlockingStub> {
    private HouseServicesBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HouseServicesBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HouseServicesBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HouseServicesBlockingStub(channel, callOptions);
    }

    /**
     */
    public protosPackage.HouseServicesOuterClass.HouseResponse hello(protosPackage.HouseServicesOuterClass.House request) {
      return blockingUnaryCall(
          getChannel(), METHOD_HELLO, getCallOptions(), request);
    }

    /**
     */
    public protosPackage.HouseServicesOuterClass.Response addStatistic(protosPackage.HouseServicesOuterClass.Statistic request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD_STATISTIC, getCallOptions(), request);
    }

    /**
     */
    public protosPackage.HouseServicesOuterClass.Response exit(protosPackage.HouseServicesOuterClass.exitHouse request) {
      return blockingUnaryCall(
          getChannel(), METHOD_EXIT, getCallOptions(), request);
    }

    /**
     * <pre>
     *election
     * </pre>
     */
    public protosPackage.HouseServicesOuterClass.Response election(protosPackage.HouseServicesOuterClass.House request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ELECTION, getCallOptions(), request);
    }

    /**
     */
    public protosPackage.HouseServicesOuterClass.Response newCoordinator(protosPackage.HouseServicesOuterClass.House request) {
      return blockingUnaryCall(
          getChannel(), METHOD_NEW_COORDINATOR, getCallOptions(), request);
    }

    /**
     * <pre>
     *extra usage
     * </pre>
     */
    public protosPackage.HouseServicesOuterClass.BoostResponse requestBoost(protosPackage.HouseServicesOuterClass.BoostRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REQUEST_BOOST, getCallOptions(), request);
    }

    /**
     */
    public protosPackage.HouseServicesOuterClass.Response reply(protosPackage.HouseServicesOuterClass.House request) {
      return blockingUnaryCall(
          getChannel(), METHOD_REPLY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HouseServicesFutureStub extends io.grpc.stub.AbstractStub<HouseServicesFutureStub> {
    private HouseServicesFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private HouseServicesFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HouseServicesFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new HouseServicesFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.HouseResponse> hello(
        protosPackage.HouseServicesOuterClass.House request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_HELLO, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.Response> addStatistic(
        protosPackage.HouseServicesOuterClass.Statistic request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD_STATISTIC, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.Response> exit(
        protosPackage.HouseServicesOuterClass.exitHouse request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_EXIT, getCallOptions()), request);
    }

    /**
     * <pre>
     *election
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.Response> election(
        protosPackage.HouseServicesOuterClass.House request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ELECTION, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.Response> newCoordinator(
        protosPackage.HouseServicesOuterClass.House request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_NEW_COORDINATOR, getCallOptions()), request);
    }

    /**
     * <pre>
     *extra usage
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.BoostResponse> requestBoost(
        protosPackage.HouseServicesOuterClass.BoostRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REQUEST_BOOST, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.HouseServicesOuterClass.Response> reply(
        protosPackage.HouseServicesOuterClass.House request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_REPLY, getCallOptions()), request);
    }
  }

  private static final int METHODID_HELLO = 0;
  private static final int METHODID_ADD_STATISTIC = 1;
  private static final int METHODID_EXIT = 2;
  private static final int METHODID_ELECTION = 3;
  private static final int METHODID_NEW_COORDINATOR = 4;
  private static final int METHODID_REQUEST_BOOST = 5;
  private static final int METHODID_REPLY = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HouseServicesImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HouseServicesImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HELLO:
          serviceImpl.hello((protosPackage.HouseServicesOuterClass.House) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.HouseResponse>) responseObserver);
          break;
        case METHODID_ADD_STATISTIC:
          serviceImpl.addStatistic((protosPackage.HouseServicesOuterClass.Statistic) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response>) responseObserver);
          break;
        case METHODID_EXIT:
          serviceImpl.exit((protosPackage.HouseServicesOuterClass.exitHouse) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response>) responseObserver);
          break;
        case METHODID_ELECTION:
          serviceImpl.election((protosPackage.HouseServicesOuterClass.House) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response>) responseObserver);
          break;
        case METHODID_NEW_COORDINATOR:
          serviceImpl.newCoordinator((protosPackage.HouseServicesOuterClass.House) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response>) responseObserver);
          break;
        case METHODID_REQUEST_BOOST:
          serviceImpl.requestBoost((protosPackage.HouseServicesOuterClass.BoostRequest) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.BoostResponse>) responseObserver);
          break;
        case METHODID_REPLY:
          serviceImpl.reply((protosPackage.HouseServicesOuterClass.House) request,
              (io.grpc.stub.StreamObserver<protosPackage.HouseServicesOuterClass.Response>) responseObserver);
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

  private static abstract class HouseServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HouseServicesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protosPackage.HouseServicesOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HouseServices");
    }
  }

  private static final class HouseServicesFileDescriptorSupplier
      extends HouseServicesBaseDescriptorSupplier {
    HouseServicesFileDescriptorSupplier() {}
  }

  private static final class HouseServicesMethodDescriptorSupplier
      extends HouseServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HouseServicesMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HouseServicesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HouseServicesFileDescriptorSupplier())
              .addMethod(METHOD_HELLO)
              .addMethod(METHOD_ADD_STATISTIC)
              .addMethod(METHOD_EXIT)
              .addMethod(METHOD_ELECTION)
              .addMethod(METHOD_NEW_COORDINATOR)
              .addMethod(METHOD_REQUEST_BOOST)
              .addMethod(METHOD_REPLY)
              .build();
        }
      }
    }
    return result;
  }
}
