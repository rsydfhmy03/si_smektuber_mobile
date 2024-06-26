package com.nekoid.smektuber.screen.home.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nekoid.smektuber.R;
import com.nekoid.smektuber.api.Endpoint;
import com.nekoid.smektuber.api.ImageUrlUtil;
import com.nekoid.smektuber.api.PublicApi;
import com.nekoid.smektuber.app.BaseActivity;
import com.nekoid.smektuber.helpers.listener.TextChangeListener;
import com.nekoid.smektuber.helpers.navigation.Navigator;
import com.nekoid.smektuber.helpers.profile.CallbackListener;
import com.nekoid.smektuber.helpers.profile.DialogChangePhotoProfile;
import com.nekoid.smektuber.helpers.utils.State;
import com.nekoid.smektuber.helpers.utils.Utils;
import com.nekoid.smektuber.models.UserModel;
import com.nekoid.smektuber.network.Http;
import com.nekoid.smektuber.network.Response;
import com.nekoid.smektuber.screen.notification.LoadingDialog;
import com.nekoid.smektuber.screen.notification.NotifNoInternet;
import com.nekoid.smektuber.screen.notification.Notif_Succes_change_Data_Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChangeDataAccount extends BaseActivity {

    ImageView iconUpload, ImageUser;

    Toolbar toolbar;

    TextInputLayout layoutCaUsername, layoutCaFullName, layoutCaEmail, layoutCaPassword, layoutCaConfirmPassword;

    TextInputEditText caUsername, caFullName, caEmail, caPassword, caConfirmPassword;

    Button btnUpdate;

    private boolean isUpdate = false;

    private UserModel userModel;

    private boolean isUpdateAvatar = false;

    private Uri uri;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data_account);
        loadingDialog = new LoadingDialog(this);
        setVariable();
        setToolbar();
        onListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();
            ImageUser.setImageURI(uri);
            isUpdateAvatar = uri != null;
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Navigator.of(this).pop(isUpdate);
        return true;
    }

    private void setVariable() {
        // User Model
        userModel = State.userModel;

        // toolbar
        toolbar = findViewById(R.id.backIcon);

        // image
        iconUpload = findViewById(R.id.IconUploadImage);
        ImageUser = findViewById(R.id.ImageUserProfil);

        // layout for input text to handle error
        layoutCaUsername = findViewById(R.id.layoutCaUsername);
        layoutCaFullName = findViewById(R.id.layoutCaFullName);
        layoutCaEmail = findViewById(R.id.layoutCaEmail);
        layoutCaPassword = findViewById(R.id.layoutCaPassword);
        layoutCaConfirmPassword = findViewById(R.id.layoutCaConfirmPassword);

        // input text
        caUsername = findViewById(R.id.Ca_username);
        caFullName = findViewById(R.id.Ca_NameLengkap);
        caEmail = findViewById(R.id.Ca_Email);
        caPassword = findViewById(R.id.Ca_password);
        caConfirmPassword = findViewById(R.id.Ca_KonfirmasiPassword);

        // button
        btnUpdate = findViewById(R.id.BtnMemperbarui);

        setModelToView();
    }

    // set model to view
    private void setModelToView() {
        if (userModel != null) {
            caUsername.setText(userModel.username);
            caFullName.setText(userModel.name);
            caEmail.setText(userModel.email);
            if (userModel.avatar != null && !userModel.avatar.isEmpty() && Utils.isUrl(userModel.avatar)) {
                Http.loadImageWithoutCache(userModel.avatar, ImageUser);
            }
        }
    }

    private String username() {
        return caUsername.getText().toString();
    }

    private String fullName() {
        return caFullName.getText().toString();
    }

    private String email() {
        return caEmail.getText().toString();
    }

    private String password() {
        return caPassword.getText().toString();
    }

    private String confirmPassword() {
        return caConfirmPassword.getText().toString();
    }

    private Map<String, String> getUpdateAccount() {
        Map<String, String> updateAccount = new HashMap<>();
        updateAccount.put("username", username());
        updateAccount.put("name", fullName());
        updateAccount.put("email", email());
        return updateAccount;
    }

    public Map<String, String> getUpdatePassword() {
        Map<String, String> updatePassword = new HashMap<>();
        updatePassword.put("old_password", getCredentials());
        updatePassword.put("password", password());
        updatePassword.put("password_confirmation", confirmPassword());
        return updatePassword;
    }

    private Map<String, File> getUpdateAvatar() {
        Map<String, File> avatar = new HashMap<>();
        if (uri != null) {
            avatar.put("avatar", new File(getExternalCacheDir(), new File(this.uri.toString()).getName()));
        } else {
            avatar.put("avatar", null);
        }
        return avatar;
    }

    /**
     * <p>Handle Error</p>
     *
     * <p>We can custom message if error detection</p>
     */
    public boolean validator() {
        String username = username();
        String fullName = fullName();

        if (username.isEmpty()) {
            layoutCaUsername.setErrorEnabled(true);
            layoutCaUsername.setError("username tidak boleh kosong");
            return false;
        }

        if (fullName.isEmpty()) {
            layoutCaFullName.setErrorEnabled(true);
            layoutCaFullName.setError("Nama lengkap tidak boleh kosong");
            return false;
        }

        if (!password().isEmpty() && !password().equals(confirmPassword())) {
            layoutCaConfirmPassword.setErrorEnabled(true);
            layoutCaConfirmPassword.setErrorIconDrawable(null);
            layoutCaConfirmPassword.setError("Konfirmasi password tidak sama !!");
            return false;
        }

        return true;
    }

    private void onListener() {
        iconUpload.setOnClickListener(v -> {
            DialogChangePhotoProfile dialog = new DialogChangePhotoProfile();
            dialog.title("Pilih Foto Profil");
            dialog.show(this, new CallbackListener(this) {
                @Override
                public void onDelete() {
                    ImageUser.setImageResource(R.drawable.iconuser);
                    isUpdateAvatar = true;
                    Http.delete(Endpoint.UPDATE_AVATAR.getUrl(),  PublicApi.getHeaders(), response -> {
                        System.out.println(response);
                    });
                }
            });
        });

        btnUpdate.setOnClickListener(v -> {

            if (!Utils.isNetworkAvailable()) {
                fragmentNoInternet();
                return;
            }
            loadingDialog.startLoading();

            if (!validator()) {
                loadingDialog.isDismiss();
                return;
            }

            if (!emailValidator(layoutCaEmail, caEmail)) {
                loadingDialog.isDismiss();
                return;
            }

            // add request for update account
            Http.put(Endpoint.UPDATE_USER.getUrl(), PublicApi.getHeaders(), getUpdateAccount(), this::doAccountUpdate);

            // user change avatar
            if (isUpdateAvatar) {
                Http.multipartFile(Endpoint.UPDATE_AVATAR.getUrl(), getUpdateAvatar(), PublicApi.getHeaders(), this::doUpdateAvatar);
                isUpdateAvatar = false;
                ImageUser.setBackgroundResource(R.drawable.iconuser);
            }

            // user want to change password
            if (isChangePassword()) {
                Http.put(Endpoint.UPDATE_PASSWORD.getUrl(), PublicApi.getHeaders(), getUpdatePassword(), this::doChangePassword);
            }

            resetViewPassword();
            isUpdate = true;
        });

        caUsername.addTextChangedListener(new TextChangeListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                if (!username().isEmpty()) {
                    layoutCaUsername.setErrorEnabled(false);
                }
            }
        });

        caFullName.addTextChangedListener(new TextChangeListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                if (!fullName().isEmpty()) {
                    layoutCaFullName.setErrorEnabled(false);
                }
            }
        });

        caEmail.addTextChangedListener(new TextChangeListener() {
            @Override
            protected void onTextChanged(String before, String old, String aNew, String after) {
                if (!email().isEmpty()) {
                    layoutCaEmail.setErrorEnabled(false);
                }
            }
        });
    }

    private void resetViewPassword() {
        caPassword.setText("");
        caConfirmPassword.setText("");
    }

    public void fragmentNoInternet() {
        findViewById(R.id.changeDataScroll).setVisibility(View.INVISIBLE);
        findViewById(R.id.changeDataFragment).setVisibility(View.VISIBLE);
        replaceFragment(R.id.changeDataFragment, new NotifNoInternet(view -> {
            if (Utils.isNetworkAvailable()) {
                findViewById(R.id.changeDataScroll).setVisibility(View.VISIBLE);
                findViewById(R.id.changeDataFragment).setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(this, "Please connect to internet, and try again", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void doAccountUpdate(Response response) {
        try {
            JSONObject body = new JSONObject(response.body.toString());
            if (response.statusCode != 200) {
                Toast.makeText(this, body.getString("message"), Toast.LENGTH_SHORT).show();
                return;
            }
            userModel = UserModel.fromJson(body.getJSONObject("data"));
            State.setUserModel(userModel);
            setModelToView();
//            Intent intent = new Intent(ChangeDataAccount.this,ChangeDataAccount.class); intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); finish();
            // you can add more action after update account

            loadingDialog.isDismiss();
            findViewById(R.id.changeDataScroll).setVisibility(View.INVISIBLE);
            findViewById(R.id.changeDataFragment).setVisibility(View.VISIBLE);
            replaceFragment(R.id.changeDataFragment, new Notif_Succes_change_Data_Account());
            Toast.makeText(this, "Berhasil memperbarui akun", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void doChangePassword(Response response) {
        try {
            JSONObject body = new JSONObject(response.body.toString());
            if (response.statusCode != 202) {
                Toast.makeText(this, body.getString("message"), Toast.LENGTH_SHORT).show();
                return;
            }
            doLogin(username(), password());
            loadingDialog.isDismiss();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void doUpdateAvatar(Response response) {
        try {
            if (response.statusCode != 200) {
//                Toast.makeText(this, "engror" + response.statusCode, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject responseBody = new JSONObject(response.body.toString());

            userModel = UserModel.fromJson(responseBody.getJSONObject("data"));
            State.setUserModel(userModel);
            setModelToView();

            loadingDialog.isDismiss();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isChangePassword() {
        if (!password().isEmpty() && !confirmPassword().isEmpty() && password().equals(confirmPassword())) {
            layoutCaConfirmPassword.setErrorEnabled(false);
            return password().equals(confirmPassword());
        }
        return false;
    }
}