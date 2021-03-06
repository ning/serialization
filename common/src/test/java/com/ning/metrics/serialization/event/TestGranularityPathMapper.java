/*
 * Copyright 2010-2011 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.ning.metrics.serialization.event;

import org.joda.time.DateTime;
import org.joda.time.MutableInterval;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;

public class TestGranularityPathMapper
{
    private static final String PREFIX = "fuu";

    @Test(groups = "fast")
    public void testMinutly()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.MINUTE);

        Assert.assertEquals(prefix.getPrefix(), PREFIX);
        Assert.assertEquals(prefix.getRootPath(), PREFIX);
        Assert.assertEquals(prefix.getGranularity(), Granularity.MINUTE);

        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01")), String.format("%s/2010/10/01/00/00", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01T07:12:11")), String.format("%s/2010/10/01/07/12", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-25T07:59:59")), String.format("%s/2010/10/25/07/59", PREFIX));
    }

    @Test(groups = "fast")
    public void testHourly()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.HOURLY);

        Assert.assertEquals(prefix.getPrefix(), PREFIX);
        Assert.assertEquals(prefix.getRootPath(), PREFIX);
        Assert.assertEquals(prefix.getGranularity(), Granularity.HOURLY);

        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01")), String.format("%s/2010/10/01/00", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01T07:12:11")), String.format("%s/2010/10/01/07", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-25T07:59:59")), String.format("%s/2010/10/25/07", PREFIX));
    }

    @Test(groups = "fast")
    public void testDaily()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.DAILY);

        Assert.assertEquals(prefix.getPrefix(), PREFIX);
        Assert.assertEquals(prefix.getRootPath(), PREFIX);
        Assert.assertEquals(prefix.getGranularity(), Granularity.DAILY);

        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01")), String.format("%s/2010/10/01", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01T07:12:11")), String.format("%s/2010/10/01", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-25T07:59:59")), String.format("%s/2010/10/25", PREFIX));
    }

    @Test(groups = "fast")
    public void testWeekly()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.WEEKLY);

        Assert.assertEquals(prefix.getPrefix(), PREFIX);
        Assert.assertEquals(prefix.getRootPath(), PREFIX);
        Assert.assertEquals(prefix.getGranularity(), Granularity.WEEKLY);

        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01")), String.format("%s/2010/10/01", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01T07:12:11")), String.format("%s/2010/10/01", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-25T07:59:59")), String.format("%s/2010/10/25", PREFIX));
    }

    @Test(groups = "fast")
    public void testMonthly()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.MONTHLY);

        Assert.assertEquals(prefix.getPrefix(), PREFIX);
        Assert.assertEquals(prefix.getRootPath(), PREFIX);
        Assert.assertEquals(prefix.getGranularity(), Granularity.MONTHLY);

        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01")), String.format("%s/2010/10", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01T07:12:11")), String.format("%s/2010/10", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-25T07:59:59")), String.format("%s/2010/10", PREFIX));
    }

    @Test(groups = "fast")
    public void testYearly()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.YEARLY);

        Assert.assertEquals(prefix.getPrefix(), PREFIX);
        Assert.assertEquals(prefix.getRootPath(), PREFIX);
        Assert.assertEquals(prefix.getGranularity(), Granularity.YEARLY);

        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01")), String.format("%s/2010", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-01T07:12:11")), String.format("%s/2010", PREFIX));
        Assert.assertEquals(prefix.getPathForDateTime(new DateTime("2010-10-25T07:59:59")), String.format("%s/2010", PREFIX));
    }

    @Test(groups = "fast")
    public void testInterval()
    {
        final GranularityPathMapper prefix = new GranularityPathMapper(PREFIX, Granularity.DAILY);

        final Collection<String> paths = prefix.getPathsForInterval(new MutableInterval(new DateTime("2010-10-01").getMillis(), new DateTime("2010-10-04").getMillis()));
        final Object[] pathsArray = paths.toArray();

        Assert.assertEquals(pathsArray.length, 3);
        Assert.assertEquals((String) pathsArray[0], String.format("%s/2010/10/01", PREFIX));
        Assert.assertEquals((String) pathsArray[1], String.format("%s/2010/10/02", PREFIX));
        Assert.assertEquals((String) pathsArray[2], String.format("%s/2010/10/03", PREFIX));
    }
}
