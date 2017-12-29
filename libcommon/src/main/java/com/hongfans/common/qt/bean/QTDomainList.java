package com.hongfans.common.qt.bean;

import java.util.List;

/**
 * TODO
 * Created by MEI on 2017/10/12.
 */

public class QTDomainList {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "QTDomainList{" +
                "data=" + data +
                '}';
    }

    public class Data{
        private RadioStation radiostations_hls;

        public RadioStation getRadiostations_hls() {
            return radiostations_hls;
        }

        public void setRadiostations_hls(RadioStation radiostations_hls) {
            this.radiostations_hls = radiostations_hls;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "radiostations_hls=" + radiostations_hls +
                    '}';
        }
    }

    public class RadioStation{
        private List<MediaCenter> mediacenters;

        public List<MediaCenter> getMediacenters() {
            return mediacenters;
        }

        public void setMediacenters(List<MediaCenter> mediacenters) {
            this.mediacenters = mediacenters;
        }

        @Override
        public String toString() {
            return "RadioStation{" +
                    "mediacenters=" + mediacenters +
                    '}';
        }
    }

    public class MediaCenter {
        private String backup_ips;
        private String test_path;

        public String getBackup_ips() {
            return backup_ips;
        }

        public void setBackup_ips(String backup_ips) {
            this.backup_ips = backup_ips;
        }

        public String getTest_path() {
            return test_path;
        }

        public void setTest_path(String test_path) {
            this.test_path = test_path;
        }

        @Override
        public String toString() {
            return "MediaCenter{" +
                    "backup_ips='" + backup_ips + '\'' +
                    ", test_path='" + test_path + '\'' +
                    '}';
        }
    }
}
