package com.triangle.pocketcoursemanager.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.triangle.pocketcoursemanager.db.dao.UserDao;
import com.triangle.pocketcoursemanager.models.User;
import com.triangle.pocketcoursemanager.models.UserTypes;

import java.util.Objects;

public class UserAuth {
    private static UserAuth instance;
    private User user;

    private UserAuth(){}

    public static UserAuth getInstance(){
        if (instance == null){
            instance = new UserAuth();
        }
        return instance;
    }

    public User getCurrentUser(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences("course.manager.auth", Context.MODE_PRIVATE);
        if (Objects.equals(sharedPref.getString("currentUserEmail", ""), "") || Objects.equals(sharedPref.getString("currentUserPassword", ""), "")){
            return null;
        }
        else loginUser(
                context,
                sharedPref.getString("currentUserEmail", ""),
                sharedPref.getString("currentUserPassword", "")
                );
        return user;
    }

    public User loginUser(Context context, String email, String password){
        UserDao dao = new UserDao(context);
        user = dao.getUser(email, password);
        if (user == null){
            return null;
        }
        SharedPreferences sharedPref = context.getSharedPreferences("course.manager.auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentUserEmail", user.getEmail());
        editor.putString("currentUserPassword", user.getPassword());
        editor.apply();

        return user;
    }

    public void logout(Context context){
        user = null;
        SharedPreferences sharedPref = context.getSharedPreferences("course.manager.auth", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("currentUserEmail", null);
        editor.putString("currentUserPassword", null);
        editor.apply();
    }

    public User registerAdmin(Context context, String email, String name, String password){
        User newUser = new User(
                0, name, email, password, "New User Description", UserTypes.ADMIN
        );
        UserDao dao = new UserDao(context);
        long result = dao.insert(newUser);
        if (result > 0){
            return newUser;
        }
        return null;
    }

    public User registerStudent(Context context, String email, String name, String password){
        User newUser = new User(
                0, name, email, password, "New User Description", UserTypes.STUDENT
        );
        UserDao dao = new UserDao(context);
        long result = dao.insert(newUser);
        if (result > 0){
            return newUser;
        }
        return null;
    }
}
