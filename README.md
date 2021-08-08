# AnimeCharactersFirebase
Kullanıcılar sevdikleri anime karakterlerinin resimlerini paylaşabilir ve yorum yazabilir.
Text hata kontrolleri yapılmış durumda. Kayıt ekranında firebase'in email kayıt sistemini kullandığım için unique email kontrolünü kendi yapıyor ve çoğu hata kontrolü kendi yapıyor.
Uygulama firebase kullanılarak oluşturuldu. Firebase veritabanında 2 adet collection var. Birincisi postları tutuyor. İkici collection ise email ile kayıt olmuş kullanıcıların nickname'lerini tutuyor. Storage kısmında teleondan yüklenen resimler tutuluyor. Resim önce firebase storage yükleniyor, firebase'in ürettiği resim indirme linki tekrar alınıp post database kayıt ediliyor. Bu şekilde postlar çekilirken picasso kullanarak urlden resimleri getirebildim.
Giriş ekranı: ![login](https://user-images.githubusercontent.com/83123957/128631514-d9193c03-6346-438b-a417-4b6ea264169c.PNG)
Kayıt ol ekranı: ![signup](https://user-images.githubusercontent.com/83123957/128631579-5e6669cf-0754-403d-b9c2-72e8c2479694.PNG)
Postlar ekranı: ![main](https://user-images.githubusercontent.com/83123957/128631605-a399e9ab-374f-43a1-9f32-65abe7967ae3.PNG)
![main2](https://user-images.githubusercontent.com/83123957/128631608-c884845b-11a4-4ec5-a5ca-5b7ff5eb8732.PNG)
Post paylaşma ekranı: ![share](https://user-images.githubusercontent.com/83123957/128631708-fbb2d33a-6e0e-4ff7-90f1-d3be1b06e4a6.PNG)
![share2](https://user-images.githubusercontent.com/83123957/128631709-f651c1f8-98d5-491f-813a-510ad6dc97a5.PNG)
En son paylaşılan akışta en üstte gelir: 
![main3](https://user-images.githubusercontent.com/83123957/128631610-384c3c7d-e764-4876-964c-a6ac71ca5c3b.PNG)
