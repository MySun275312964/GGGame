#!/usr/bin/env bash

protoc --java_out=../src/main/java PSessionManager.proto
protoc --java_out=../src/main/java Test.proto
