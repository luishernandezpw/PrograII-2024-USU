package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.CompletableFuture;

public class Token {
    public String obtenerToken(){
        CompletableFuture<String> tarea = new CompletableFuture<>();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if( task.isSuccessful() ){
                    tarea.complete(task.getResult());
                }else{
                    tarea.completeExceptionally(task.getException());
                }
            }
        });
        try {
            return tarea.get();
        }catch (Exception e){
            return null;
        }
    }
}
