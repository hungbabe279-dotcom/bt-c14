# Deployment Instructions

## Tổng quan

Ứng dụng đã được chuẩn bị sẵn sàng để deploy lên Render.com

## Các bước Deploy lên Render

### 1. Tạo GitHub Repository

```bash
cd ch13_ex1_email
git init
git add .
git commit -m "Initial commit: Email List Application"
```

Sau đó push lên GitHub:
```bash
git remote add origin https://github.com/YOUR_USERNAME/ch13-email-list.git
git branch -M main
git push -u origin main
```

### 2. Connect với Render

1. Đăng nhập vào [Render.com](https://render.com)
2. Click "New +" → "Web Service"
3. Connect your GitHub account
4. Select repository: `ch13-email-list`
5. Fill in deployment settings:
   - **Name**: `email-list-app`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean package`
   - **Start Command**: `java -jar target/email-list.jar`
   - **Instance Type**: Free (đủ cho ứng dụng nhỏ)

### 3. Deploy

- Click "Deploy"
- Render sẽ tự động:
  - Clone code từ GitHub
  - Chạy Maven build
  - Start ứng dụng
  - Assign một public URL

### 4. Truy cập ứng dụng

Sau khi deploy xong, bạn sẽ có một URL như:
```
https://email-list-app.onrender.com
```

## Lưu ý quan trọng

1. **Port**: Ứng dụng sẽ tự động lắng nghe port từ environment variable `PORT` (được set bởi Render)
2. **In-Memory Database**: Dữ liệu sẽ bị mất khi ứng dụng restart. Để persistent, thêm database.
3. **Free Plan**: 
   - Ứng dụng sẽ sleep sau 15 phút không dùng
   - Cần 50 giờ/tháng để active
4. **Build Time**: Lần build đầu có thể lâu (30+ phút)

## File cấu hình

- **Procfile** - Hướng dẫn Render cách start ứng dụng
- **pom.xml** - Maven configuration with assembly plugin
- **.gitignore** - File không cần commit lên GitHub

## Troubleshooting

Nếu deploy fail, check logs trên Render dashboard:
1. Build logs - lỗi compile Maven
2. Deploy logs - lỗi khi khởi động ứng dụng

Các lỗi thường gặp:
- **Java version**: Render hỗ trợ Java 8, 11, 17, 21
- **PORT variable**: Đảm bảo code đọc PORT từ environment
- **Build artifacts**: Đảm bảo assembly plugin đúng

## Local Testing

Trước khi deploy lên Render, test local:

```bash
mvn clean package
java -jar target/email-list.jar
```

Mở browser: `http://localhost:8080`

## Alternative Platforms

Ngoài Render, có thể deploy lên:
- **Heroku** (free tier đã ngừng)
- **Railway.app**
- **Replit**
- **Gitpod**
