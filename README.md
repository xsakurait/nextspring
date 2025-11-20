# YouTube Channel Manager

YouTube チャンネルを整理し、新着動画を効率的に管理する Web アプリケーション

## 機能

- ✅ YouTube チャンネルの購読管理
- ✅ カテゴリ別チャンネル整理
- ✅ 新着動画の一覧表示
- ✅ 動画のソート機能（公開日、再生回数、高評価数）
- ✅ ホバー時のポップアップ詳細表示
- ✅ YouTube API 連携
- ✅ JWT 認証

## 技術スタック

### バックエンド

- Spring Boot 3.2
- Spring Security (JWT 認証)
- Spring Data JPA
- MyBatis
- MapStruct
- MySQL 8.0
- YouTube Data API v3

### フロントエンド

- Next.js 14 (App Router)
- React 18
- TypeScript
- Tailwind CSS
- Zustand (状態管理)
- Axios

### インフラ

- Docker
- Kubernetes
- ArgoCD
- GitHub Actions
- Railway (デプロイ)

## セットアップ

### 前提条件

- Docker Desktop
- VS Code
- Git
- YouTube API Key

### 1. リポジトリのクローン

\`\`\`bash
git clone https://github.com/your-username/nextspring.git
cd nextspring
\`\`\`

### 2. 環境変数の設定

\`\`\`bash

# .env ファイルを作成

cp .env.example .env

# YouTube API Key を設定

YOUTUBE_API_KEY=your-api-key-here
JWT_SECRET=your-secret-key-here
\`\`\`

### 3. DevContainer で開く

1. VS Code でプロジェクトを開く
2. コマンドパレット (Ctrl+Shift+P) を開く
3. "Dev Containers: Reopen in Container" を選択

### 4. データベースの初期化

DevContainer 内で自動的に MySQL が起動し、初期化スクリプトが実行されます。

### 5. アプリケーションの起動

#### バックエンド (Spring Boot)

\`\`\`bash
cd backend
mvn spring-boot:run
\`\`\`

#### フロントエンド (Next.js)

\`\`\`bash
cd frontend
npm install
npm run dev
\`\`\`

### 6. アクセス

- フロントエンド: http://localhost:3000
- バックエンド API: http://localhost:8080/api

## Docker Compose での起動

\`\`\`bash
docker-compose up -d
\`\`\`

## Kubernetes へのデプロイ

### 1. Secrets の作成

\`\`\`bash
kubectl create secret generic app-secrets \
 --from-literal=YOUTUBE_API_KEY=your-api-key \
 --from-literal=JWT_SECRET=your-jwt-secret \
 -n youtube-manager
\`\`\`

### 2. マニフェストの適用

\`\`\`bash
kubectl apply -f k8s/
\`\`\`

### 3. ArgoCD での管理

\`\`\`bash
kubectl apply -f argocd/application.yaml
\`\`\`

## コンテナ内から Git にプッシュ

DevContainer 内で Git を使用する場合:

\`\`\`bash

# Git 設定

git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# SSH キーの設定（推奨）

ssh-keygen -t ed25519 -C "your.email@example.com"
cat ~/.ssh/id_ed25519.pub

# 公開鍵を GitHub に追加

# コミットとプッシュ

git add .
git commit -m "Your commit message"
git push
\`\`\`

## Railway へのデプロイ

1. Railway アカウントを作成
2. GitHub リポジトリを接続
3. 環境変数を設定:
   - `YOUTUBE_API_KEY`
   - `JWT_SECRET`
   - `MYSQL_ROOT_PASSWORD`
4. デプロイを実行

## 開発ワークフロー

1. 機能ブランチを作成
2. コードを変更
3. コミット・プッシュ
4. GitHub Actions が自動的にビルド・テスト
5. PR をマージ
6. ArgoCD が自動的にデプロイ

## VS Code 拡張機能

DevContainer には以下の拡張機能が自動インストールされます:

- Spring Boot Extension Pack
- Java Extension Pack
- ES7 React/Redux/GraphQL/React-Native snippets
- ESLint
- Prettier
- Auto Rename Tag
- Path Intellisense
- Code Runner
- MySQL Client
- Docker
- GitLens
- Cloud Code (Gemini Code Assist)

## ライセンス

MIT
