package com.moonface.Util;

import java.util.ArrayList;
import java.util.HashMap;


public class ListUtil {
    public static ArrayList<HashMap<String, Object>> filterMapList(ArrayList<HashMap<String, Object>> _list, String _key, Object _val) {
        {
            ArrayList<HashMap<String, Object>> _outputlist = new ArrayList<>(_list);
            for (int i = 0; i < _outputlist.size(); i++) {
                if (!(_outputlist.get(i).get(_key).toString().toLowerCase().contains(_val.toString().toLowerCase()))) {
                    _outputlist.remove(i);
                }
            }
            return _outputlist;
        }
    }
    public static ArrayList<Object> filterList(ArrayList<Object> _list, Object _val) {
        {
            ArrayList<Object> _outputlist = new ArrayList<>(_list);
            for (int i = 0; i < _outputlist.size(); i++) {
                if (!(_outputlist.get(i).toString().toLowerCase().contains(_val.toString().toLowerCase()))) {
                    _outputlist.remove(i);
                }
            }
            return _outputlist;
        }
    }
}

