-if class com.example.animeapp.model.Trailer
-keepnames class com.example.animeapp.model.Trailer
-if class com.example.animeapp.model.Trailer
-keep class com.example.animeapp.model.TrailerJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
