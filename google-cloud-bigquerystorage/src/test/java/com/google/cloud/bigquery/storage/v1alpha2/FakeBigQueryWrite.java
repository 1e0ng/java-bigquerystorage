/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigquery.storage.v1alpha2;

import com.google.api.gax.grpc.testing.MockGrpcService;
import com.google.cloud.bigquery.storage.v1alpha2.Storage.*;
import com.google.protobuf.AbstractMessage;
import io.grpc.ServerServiceDefinition;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import org.threeten.bp.Duration;

/**
 * A fake implementation of {@link MockGrpcService}, that can be used to test clients of a
 * StreamWriter. It forwards calls to the real implementation (@link FakeBigQueryWriteImpl}.
 */
public class FakeBigQueryWrite implements MockGrpcService {
  private final FakeBigQueryWriteImpl serviceImpl;

  public FakeBigQueryWrite() {
    serviceImpl = new FakeBigQueryWriteImpl();
  }

  @Override
  public List<AbstractMessage> getRequests() {
    return new LinkedList<AbstractMessage>(serviceImpl.getCapturedRequests());
  }

  public List<AppendRowsRequest> getAppendRequests() {
    return serviceImpl.getCapturedRequests();
  }

  @Override
  public void addResponse(AbstractMessage response) {
    if (response instanceof AppendRowsResponse) {
      serviceImpl.addResponse((AppendRowsResponse) response);
    } else {
      throw new IllegalStateException("Unsupported service");
    }
  }

  @Override
  public void addException(Exception exception) {
    serviceImpl.addConnectionError(exception);
  }

  @Override
  public ServerServiceDefinition getServiceDefinition() {
    return serviceImpl.bindService();
  }

  @Override
  public void reset() {
    serviceImpl.reset();
  }

  public void setResponseDelay(Duration delay) {
    serviceImpl.setResponseDelay(delay);
  }

  public void setExecutor(ScheduledExecutorService executor) {
    serviceImpl.setExecutor(executor);
  }
}