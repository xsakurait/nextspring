#!/bin/bash

# YouTube Channel Manager - 開発環境起動スクリプト

set -e

echo "🚀 YouTube Channel Manager 開発環境を起動します..."

# 環境変数の確認
if [ ! -f .env ]; then
    echo "⚠️  .env ファイルが見つかりません"
    echo "📝 .env.example から .env を作成してください"
    cp .env.example .env
    echo "✅ .env ファイルを作成しました。編集してください。"
    exit 1
fi

# Docker Composeでサービスを起動
echo "🐳 Docker Composeでサービスを起動中..."
docker-compose up -d mysql

# MySQLの起動を待つ
echo "⏳ MySQLの起動を待っています..."
sleep 10

# Spring Bootの起動
echo "🍃 Spring Bootを起動中..."
cd backend
mvn spring-boot:run &
SPRING_PID=$!
cd ..

# Next.jsの起動
echo "⚛️  Next.jsを起動中..."
cd frontend
npm run dev &
NEXTJS_PID=$!
cd ..

echo ""
echo "✅ すべてのサービスが起動しました！"
echo ""
echo "📱 フロントエンド: http://localhost:3000"
echo "🔧 バックエンド: http://localhost:8080/api"
echo "🗄️  MySQL: localhost:3306"
echo ""
echo "停止するには Ctrl+C を押してください"
echo ""

# 終了時のクリーンアップ
trap "echo '🛑 サービスを停止中...'; kill $SPRING_PID $NEXTJS_PID; docker-compose down; exit" INT TERM

wait
