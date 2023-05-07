// Generated by view binder compiler. Do not edit!
package com.permissionx.guolindev.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.permissionx.guolindev.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class PermissionxPermissionItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView permissionIcon;

  @NonNull
  public final TextView permissionText;

  private PermissionxPermissionItemBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView permissionIcon, @NonNull TextView permissionText) {
    this.rootView = rootView;
    this.permissionIcon = permissionIcon;
    this.permissionText = permissionText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static PermissionxPermissionItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static PermissionxPermissionItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.permissionx_permission_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static PermissionxPermissionItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.permissionIcon;
      ImageView permissionIcon = ViewBindings.findChildViewById(rootView, id);
      if (permissionIcon == null) {
        break missingId;
      }

      id = R.id.permissionText;
      TextView permissionText = ViewBindings.findChildViewById(rootView, id);
      if (permissionText == null) {
        break missingId;
      }

      return new PermissionxPermissionItemBinding((LinearLayout) rootView, permissionIcon,
          permissionText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}