/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package sparksoniq;

import java.io.IOException;
import java.util.Arrays;

/*
GENERIC LAUNCH COMMAND
spark-submit --class sparksoniq.ShellStart     --master yarn-client     --deploy-mode client
--num-executors 40 --conf spark.yarn.maxAppAttempts=1 --conf spark.ui.port=4051
--conf spark.executor.memory=10g --conf spark.executor.heartbeatInterval=3600s
--conf spark.network.timeout=3600s
jsoniq-spark-app-1.0-jar-with-dependencies.jar --result-size 1000


spark-submit --class sparksoniq.ShellStart  --master local[*]  --deploy-mode client jsoniq-spark-app-1.0-jar-with-dependencies.jar  --result-size 1000

 */

public class ShellStart {
    public static void main(String[] args) throws IOException {
        String[] newargs = Arrays.copyOf(args, args.length + 2);
        newargs[args.length] = "--shell";
        newargs[args.length+1] = "yes";
        Main.main(newargs);
    }
}
