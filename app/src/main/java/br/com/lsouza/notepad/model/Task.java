package br.com.lsouza.notepad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Task implements Parcelable{

    private String id;

    private String userId;

    private String titulo;

    private String descricao;

    private Boolean isPrioridade;

    public Task(){

    }

    public Task(String id, String userId, String titulo, String descricao, Boolean isPrioridade) {
        this.id = id;
        this.userId = userId;
        this.titulo = titulo;
        this.descricao = descricao;
        this.isPrioridade = isPrioridade;
    }


    protected Task(Parcel in) {
        id = in.readString();
        userId = in.readString();
        titulo = in.readString();
        descricao = in.readString();
        byte tmpIsPrioridade = in.readByte();
        isPrioridade = tmpIsPrioridade == 0 ? null : tmpIsPrioridade == 1;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPrioridade() {
        return isPrioridade;
    }

    public void setPrioridade(Boolean prioridade) {
        isPrioridade = prioridade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeString(titulo);
        parcel.writeString(descricao);
        parcel.writeByte((byte) (isPrioridade == null ? 0 : isPrioridade ? 1 : 2));
    }
}