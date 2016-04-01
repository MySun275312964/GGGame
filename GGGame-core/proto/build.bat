protoc.exe --java_out=../src/main/java Harbor.proto
protoc.exe --plugin=protoc-gen-grpc-java=protoc-gen-grpc-java.exe --grpc-java_out=../src/main/java --proto_path=./ Harbor.proto

protoc.exe --java_out=../src/main/java Master.proto
protoc.exe --plugin=protoc-gen-grpc-java=protoc-gen-grpc-java.exe --grpc-java_out=../src/main/java --proto_path=./ Master.proto
