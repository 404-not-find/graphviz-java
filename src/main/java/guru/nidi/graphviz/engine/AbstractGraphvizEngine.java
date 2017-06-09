/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.graphviz.engine;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGraphvizEngine implements GraphvizEngine {
    private final CountDownLatch state;
    private final EngineInitListener engineInitListener;
    private Exception initException;

    public AbstractGraphvizEngine(boolean sync, EngineInitListener engineInitListener) {
        state = new CountDownLatch(1);
        this.engineInitListener = engineInitListener;
        if (sync) {
            init();
        } else {
            final Thread starter = new Thread(this::init);
            starter.setDaemon(true);
            starter.start();
        }
    }

    public String execute(String src, Options options) {
        checkInited();
        try {
            if (!state.await(60, TimeUnit.SECONDS)) {
                throw new GraphvizException("Initializing graphviz engine took too long");
            }
        } catch (InterruptedException e) {
            //ignore
        }
        checkInited();
        return doExecute(src, options);
    }

    private void checkInited() {
        if (initException != null) {
            throw new GraphvizException("Could not start graphviz engine", initException);
        }
    }

    private void init() {
        try {
            doInit();
        } catch (Exception e) {
            initException = e;
            if (engineInitListener != null) {
                engineInitListener.engineInitException(e);
            }
        } finally {
            state.countDown();
        }
    }

    @Override
    public void release() {
    }

    protected abstract void doInit() throws Exception;

    protected abstract String doExecute(String src, Options options);
}
