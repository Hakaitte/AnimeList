-if class com.example.animeapp.model.AnimeResponse
-keepnames class com.example.animeapp.model.AnimeResponse
-if class com.example.animeapp.model.AnimeResponse
-keep class com.example.animeapp.model.AnimeResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.example.animeapp.model.AnimeResponse
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.example.animeapp.model.AnimeResponse
-keepclassmembers class com.example.animeapp.model.AnimeResponse {
    public synthetic <init>(com.example.animeapp.model.Pagination,java.util.List,java.lang.Integer,java.lang.String,java.lang.String,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
