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
    comments = "Source: AdminServices.proto")
public final class AdminServicesGrpc {

  private AdminServicesGrpc() {}

  public static final String SERVICE_NAME = "protosPackage.AdminServices";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<protosPackage.AdminServicesOuterClass.HouseNP,
      protosPackage.AdminServicesOuterClass.ResponseNP> METHOD_NOTIFY =
      io.grpc.MethodDescriptor.<protosPackage.AdminServicesOuterClass.HouseNP, protosPackage.AdminServicesOuterClass.ResponseNP>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "protosPackage.AdminServices", "notify"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.AdminServicesOuterClass.HouseNP.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              protosPackage.AdminServicesOuterClass.ResponseNP.getDefaultInstance()))
          .setSchemaDescriptor(new AdminServicesMethodDescriptorSupplier("notify"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AdminServicesStub newStub(io.grpc.Channel channel) {
    return new AdminServicesStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AdminServicesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AdminServicesBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AdminServicesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AdminServicesFutureStub(channel);
  }

  /**
   */
  public static abstract class AdminServicesImplBase implements io.grpc.BindableService {

    /**
     */
    public void notify(protosPackage.AdminServicesOuterClass.HouseNP request,
        io.grpc.stub.StreamObserver<protosPackage.AdminServicesOuterClass.ResponseNP> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_NOTIFY, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_NOTIFY,
            asyncUnaryCall(
              new MethodHandlers<
                protosPackage.AdminServicesOuterClass.HouseNP,
                protosPackage.AdminServicesOuterClass.ResponseNP>(
                  this, METHODID_NOTIFY)))
          .build();
    }
  }

  /**
   */
  public static final class AdminServicesStub extends io.grpc.stub.AbstractStub<AdminServicesStub> {
    private AdminServicesStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AdminServicesStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AdminServicesStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AdminServicesStub(channel, callOptions);
    }

    /**
     */
    public void notify(protosPackage.AdminServicesOuterClass.HouseNP request,
        io.grpc.stub.StreamObserver<protosPackage.AdminServicesOuterClass.ResponseNP> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_NOTIFY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AdminServicesBlockingStub extends io.grpc.stub.AbstractStub<AdminServicesBlockingStub> {
    private AdminServicesBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AdminServicesBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AdminServicesBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AdminServicesBlockingStub(channel, callOptions);
    }

    /**
     */
    public protosPackage.AdminServicesOuterClass.ResponseNP notify(protosPackage.AdminServicesOuterClass.HouseNP request) {
      return blockingUnaryCall(
          getChannel(), METHOD_NOTIFY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AdminServicesFutureStub extends io.grpc.stub.AbstractStub<AdminServicesFutureStub> {
    private AdminServicesFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AdminServicesFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AdminServicesFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AdminServicesFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protosPackage.AdminServicesOuterClass.ResponseNP> notify(
        protosPackage.AdminServicesOuterClass.HouseNP request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_NOTIFY, getCallOptions()), request);
    }
  }

  private static final int METHODID_NOTIFY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AdminServicesImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AdminServicesImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NOTIFY:
          serviceImpl.notify((protosPackage.AdminServicesOuterClass.HouseNP) request,
              (io.grpc.stub.StreamObserver<protosPackage.AdminServicesOuterClass.ResponseNP>) responseObserver);
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

  private static abstract class AdminServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AdminServicesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protosPackage.AdminServicesOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AdminServices");
    }
  }

  private static final class AdminServicesFileDescriptorSupplier
      extends AdminServicesBaseDescriptorSupplier {
    AdminServicesFileDescriptorSupplier() {}
  }

  private static final class AdminServicesMethodDescriptorSupplier
      extends AdminServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AdminServicesMethodDescriptorSupplier(String methodName) {
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
      synchronized (AdminServicesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AdminServicesFileDescriptorSupplier())
              .addMethod(METHOD_NOTIFY)
              .build();
        }
      }
    }
    return result;
  }
}
