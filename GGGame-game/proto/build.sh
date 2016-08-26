#!/usr/bin/env bash

protoc --java_out=../src/main/java SessionManager.proto
protoc --java_out=../src/main/java Test.proto
