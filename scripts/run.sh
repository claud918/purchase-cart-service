#!/bin/bash

echo "Running the application..."
java -jar target/purchase-cart-service-0.0.1-SNAPSHOT.jar --server.port=9090
echo "Application stopped."