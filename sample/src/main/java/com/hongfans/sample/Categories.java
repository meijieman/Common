package com.hongfans.sample;

import java.util.List;

/**
 * 作者:meijie
 * 包名:com.hongfans.sample
 * 工程名:Common
 * 时间:2018/7/5 17:19
 * 说明:
 */
public class Categories{

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError(){ return error;}

    public void setError(boolean error){ this.error = error;}

    public List<ResultsBean> getResults(){ return results;}

    public void setResults(List<ResultsBean> results){ this.results = results;}

    public static class ResultsBean{

        private String _id;
        private String en_name;
        private String name;
        private int rank;

        public String get_id(){ return _id;}

        public void set_id(String _id){ this._id = _id;}

        public String getEn_name(){ return en_name;}

        public void setEn_name(String en_name){ this.en_name = en_name;}

        public String getName(){ return name;}

        public void setName(String name){ this.name = name;}

        public int getRank(){ return rank;}

        public void setRank(int rank){ this.rank = rank;}

        @Override
        public String toString(){
            return "ResultsBean{" +
                    "_id='" + _id + '\'' +
                    ", en_name='" + en_name + '\'' +
                    ", name='" + name + '\'' +
                    ", rank=" + rank +
                    '}';
        }
    }

    @Override
    public String toString(){
        return "Categories{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
