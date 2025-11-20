@echo off
REM YouTube Channel Manager - 開発環境起動スクリプト (Windows)

echo 🚀 YouTube Channel Manager 開発環境を起動します...

REM 環境変数の確認
if not exist .env (
    echo ⚠️  .env ファイルが見つかりません
    echo 📝 .env.example から .env を作成してください
    copy .env.example .env
    echo ✅ .env ファイルを作成しました。編集してください。
    exit /b 1
)

REM Docker Composeでサービスを起動
echo 🐳 Docker Composeでサービスを起動中...
docker-compose up -d mysql

REM MySQLの起動を待つ
echo ⏳ MySQLの起動を待っています...
timeout /t 10 /nobreak

REM Spring Bootの起動
echo 🍃 Spring Bootを起動中...
start cmd /k "cd backend && mvn spring-boot:run"

REM Next.jsの起動
echo ⚛️  Next.jsを起動中...
start cmd /k "cd frontend && npm run dev"

echo.
echo ✅ すべてのサービスが起動しました！
echo.
echo 📱 フロントエンド: http://localhost:3000
echo 🔧 バックエンド: http://localhost:8080/api
echo 🗄️  MySQL: localhost:3306
echo.
echo 停止するには各ウィンドウで Ctrl+C を押してください
echo.

pause
