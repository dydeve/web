/**
 * Copyright 2012 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dydeve.common.hystrix.demo;

import static org.junit.Assert.*;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import dydeve.common.util.ThrowableUtils;
import org.junit.Test;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.web.client.RestClientException;

import java.util.concurrent.TimeoutException;

/**
 * Sample {@link HystrixCommand} that does not have a fallback implemented
 * so will "fail fast" when failures, rejections, short-circuiting etc occur.
 */
public class CommandThatFailsFast extends HystrixCommand<String> {

    private final boolean throwException;

    public CommandThatFailsFast(boolean throwException) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.throwException = throwException;
    }

    @Override
    protected String run() throws TimeoutException, ReflectiveOperationException {
        if (throwException) {
            //throw new HystrixRuntimeException("failure from CommandThatFailsFast");
            //throw new HystrixBadRequestException("failure from CommandThatFailsFast");
            //throw new RuntimeException("failure from CommandThatFailsFast");
            throw new ReflectiveOperationException("failure from CommandThatFailsFast");
        } else {
            return "success";
        }
    }

    @Override
    protected String getFallback() {
        return a();
    }

    private String a() {
        throw new UnsupportedOperationException("No fallback available.");
    }

    @Override
    protected String getCacheKey() {
        return super.getCacheKey();
    }

    public static class UnitTest {

        @Test
        public void testSuccess() {
            assertEquals("success", new CommandThatFailsFast(false).execute());
        }

        @Test
        public void testFailure() {
            try {
                String a = new CommandThatFailsFast(true).execute();
                a = a;
                //fail("we should have thrown an exception");
            } catch (Exception e) {
                //assertEquals("failure from CommandThatFailsFast", e.getCause().getMessage());
                e.printStackTrace();
                System.out.println(ThrowableUtils.stackTrace(e));
            }
        }
    }
}
