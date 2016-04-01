protoc.exe --java_out=../src/main/java GateRpc.proto

protoc.exe --plugin=protoc-gen-grpc-java=protoc-gen-grpc-java.exe --grpc-java_out=../src/main/java --proto_path=./ GateRpc.proto

protoc.exe --java_out=../src/main/java GeoX.proto

protoc.exe --plugin=protoc-gen-grpc-java=protoc-gen-grpc-java.exe --grpc-java_out=../src/main/java --proto_path=./ GeoX.proto
