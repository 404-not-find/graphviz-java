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
package guru.nidi.graphviz.executor;

import org.apache.commons.exec.CommandLine;

import java.io.File;
import java.io.IOException;

/**
 * Created by toon on 07/02/17.
 */
public class DummyExecutor implements ICommandExecutor {

    @Override
    public int execute(CommandLine cmd, File workingDirectory) throws InterruptedException, IOException {
        System.out.println("CMD: " + cmd.toString());
        return 0;
    }
}
