/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.extension.siddhi.execution.throughput.util.filewriting;

import org.HdrHistogram.Histogram;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Writer;


/**
 *
 */
public class LatencyFileWriting implements Runnable {
    private final Logger log = Logger.getLogger(LatencyFileWriting.class);
    private long recordWindow = 10000;
    private long timeSpent = 0;
    private long totalTimeSpent = 0;
    private long eventCountTotal = 0;
    private long eventCount = 0;
    private boolean flag;
    private Writer fstream = null;

    Histogram histogram = new Histogram(2);
    Histogram histogram2 = new Histogram(2);


    public LatencyFileWriting(int recordWindow, long eventCountTotal, long eventCount,
                              long timeSpent, long totalTimeSpent, Histogram histogram, Histogram histogram2,
                              Writer fstream) {
        this.recordWindow = recordWindow;
        this.eventCountTotal = eventCountTotal;
        this.eventCount = eventCount;
        this.timeSpent = timeSpent;
        this.totalTimeSpent = totalTimeSpent;
        this.histogram = histogram;
        this.histogram2 = histogram2;
        this.fstream = fstream;


    }

    @Override public void run() {
        try {
            //log.info("total events" + eventCountTotal);


            fstream.write(
                    ((eventCountTotal / recordWindow) + "," + timeSpent * 1.0
                            / eventCount) +
                            "," + ((totalTimeSpent * 1.0) / eventCountTotal) + "," +
                            eventCountTotal + "," + timeSpent + "," + totalTimeSpent + "," + histogram
                            .getValueAtPercentile
                                    (90.0) + ","
                            + histogram
                            .getValueAtPercentile(95.0) + "," + histogram
                            .getValueAtPercentile(99.0)
                            + ","
                            + "" + histogram2.getValueAtPercentile(90.0) + ","
                            + "" + histogram2.getValueAtPercentile(95.0) + ","
                            + "" + histogram2.getValueAtPercentile(99.0));
            fstream.write("\r\n");


            fstream.flush();
        } catch (IOException ex) {
            log.error("Error while writing into the file" + ex.getMessage(), ex);
        }
    }


}
