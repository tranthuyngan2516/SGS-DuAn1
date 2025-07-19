package poly.cafe.dao.impl;

import poly.cafe.dao.NguoiThueDAO;
import poly.cafe.entity.NguoiThue;
import poly.cafe.util.HttpUtils;
import poly.cafe.util.SupabaseConfig;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NguoiThueDAOImpl implements NguoiThueDAO {
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new poly.cafe.ui.test.LocalDateTimeAdapter())
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();

    @Override
    public List<NguoiThue> findAll() {
        String response = HttpUtils.sendGet(SupabaseConfig.BASE_URL + "/nguoi_thue?select=*", SupabaseConfig.API_KEY);
        return gson.fromJson(response, new TypeToken<List<NguoiThue>>(){}.getType());
    }
    @Override
    public NguoiThue findById(UUID id) {
        String url = SupabaseConfig.BASE_URL + "/nguoi_thue?ma_nguoi_thue=eq." + id + "&select=*";
        String response = HttpUtils.sendGet(url, SupabaseConfig.API_KEY);
        List<NguoiThue> list = gson.fromJson(response, new TypeToken<List<NguoiThue>>(){}.getType());
        return list.isEmpty() ? null : list.get(0);
    }
    @Override
    public void insert(NguoiThue nguoiThue) {
        HttpUtils.sendPost(SupabaseConfig.BASE_URL + "/nguoi_thue", gson.toJson(nguoiThue), SupabaseConfig.API_KEY);
    }
    @Override
    public void update(NguoiThue nguoiThue) {
        String url = SupabaseConfig.BASE_URL + "/nguoi_thue?ma_nguoi_thue=eq." + nguoiThue.getMaNguoiThue();
        java.util.Map<String, Object> updateFields = new java.util.HashMap<>();
        updateFields.put("ho_ten", nguoiThue.getHoTen());
        updateFields.put("so_dien_thoai", nguoiThue.getSoDienThoai());
        updateFields.put("cmnd_cccd", nguoiThue.getCmndCccd());
        String json = gson.toJson(updateFields);
        // Debug log
        System.out.println("[DEBUG] PATCH URL: " + url);
        System.out.println("[DEBUG] PATCH UUID: " + nguoiThue.getMaNguoiThue());
        System.out.println("[DEBUG] PATCH BODY: " + json);
        HttpUtils.sendPatch(url, json, SupabaseConfig.API_KEY);
    }
    @Override
    public void delete(UUID id) {
        String url = SupabaseConfig.BASE_URL + "/nguoi_thue?ma_nguoi_thue=eq." + id;
        HttpUtils.sendDelete(url, SupabaseConfig.API_KEY);
    }
} 