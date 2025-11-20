# セットアップガイド

## 段階的セットアップ手順

### フェーズ 1: 環境準備

#### 1.1 必要なツールのインストール

- [ ] Docker Desktop をインストール
- [ ] VS Code をインストール
- [ ] Git をインストール
- [ ] VS Code の Dev Containers 拡張機能をインストール

#### 1.2 YouTube API キーの取得

1. [Google Cloud Console](https://console.cloud.google.com/) にアクセス
2. 新しいプロジェクトを作成
3. YouTube Data API v3 を有効化
4. 認証情報から API キーを作成
5. API キーをコピー

### フェーズ 2: プロジェクトのセットアップ

#### 2.1 リポジトリのクローン

\`\`\`bash
git clone https://github.com/your-username/nextspring.git
cd nextspring
\`\`\`

#### 2.2 環境変数の設定

\`\`\`bash
cp .env.example .env
\`\`\`

`.env` ファイルを編集:

\`\`\`env
YOUTUBE_API_KEY=取得した API キー
JWT_SECRET=ランダムな 256 ビット以上の文字列
\`\`\`

#### 2.3 DevContainer で開く

1. VS Code でプロジェクトフォルダを開く
2. 左下の緑色のアイコンをクリック
3. "Reopen in Container" を選択
4. コンテナのビルドと起動を待つ（初回は 10-15 分程度）

### フェーズ 3: データベースのセットアップ

DevContainer 内で自動的に実行されますが、手動で確認する場合:

\`\`\`bash

# MySQL に接続

mysql -h localhost -u root -prootpassword

# データベースの確認

SHOW DATABASES;
USE youtube_manager;
SHOW TABLES;
\`\`\`

### フェーズ 4: バックエンドの起動

#### 4.1 依存関係のインストール

\`\`\`bash
cd backend
mvn clean install
\`\`\`

#### 4.2 アプリケーションの起動

\`\`\`bash
mvn spring-boot:run
\`\`\`

#### 4.3 動作確認

ブラウザで http://localhost:8080/api にアクセス

### フェーズ 5: フロントエンドの起動

#### 5.1 依存関係のインストール

\`\`\`bash
cd frontend
npm install
\`\`\`

#### 5.2 アプリケーションの起動

\`\`\`bash
npm run dev
\`\`\`

#### 5.3 動作確認

ブラウザで http://localhost:3000 にアクセス

### フェーズ 6: 初期データの投入

1. http://localhost:3000/login にアクセス
2. 新規登録でアカウントを作成
3. ログイン
4. チャンネルを追加
5. 動画を同期

### フェーズ 7: Docker Compose での起動（オプション）

\`\`\`bash

# すべてのサービスを起動

docker-compose up -d

# ログの確認

docker-compose logs -f

# 停止

docker-compose down
\`\`\`

### フェーズ 8: Kubernetes へのデプロイ（本番環境）

#### 8.1 Kubernetes クラスタの準備

\`\`\`bash

# Minikube の場合

minikube start --cpus=4 --memory=8192

# または GKE, EKS, AKS などのマネージドサービス

\`\`\`

#### 8.2 Secrets の作成

\`\`\`bash
kubectl create namespace youtube-manager

kubectl create secret generic app-secrets \
 --from-literal=YOUTUBE_API_KEY=your-api-key \
 --from-literal=JWT_SECRET=your-jwt-secret \
 -n youtube-manager
\`\`\`

#### 8.3 マニフェストの適用

\`\`\`bash
kubectl apply -f k8s/
\`\`\`

#### 8.4 デプロイの確認

\`\`\`bash
kubectl get pods -n youtube-manager
kubectl get services -n youtube-manager
\`\`\`

### フェーズ 9: ArgoCD のセットアップ

#### 9.1 ArgoCD のインストール

\`\`\`bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
\`\`\`

#### 9.2 ArgoCD UI へのアクセス

\`\`\`bash
kubectl port-forward svc/argocd-server -n argocd 8080:443
\`\`\`

ブラウザで https://localhost:8080 にアクセス

#### 9.3 初期パスワードの取得

\`\`\`bash
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
\`\`\`

#### 9.4 Application の作成

\`\`\`bash
kubectl apply -f argocd/application.yaml
\`\`\`

### フェーズ 10: GitHub Actions のセットアップ

#### 10.1 GitHub Secrets の設定

GitHub リポジトリの Settings > Secrets and variables > Actions で以下を追加:

- `YOUTUBE_API_KEY`: YouTube API キー
- `JWT_SECRET`: JWT シークレット
- `NEXT_PUBLIC_API_URL`: フロントエンド API の URL

#### 10.2 ワークフローの確認

コードをプッシュすると自動的にビルド・テスト・デプロイが実行されます。

### フェーズ 11: Railway へのデプロイ

#### 11.1 Railway アカウントの作成

[Railway](https://railway.app/) でアカウントを作成

#### 11.2 プロジェクトの作成

1. "New Project" をクリック
2. "Deploy from GitHub repo" を選択
3. リポジトリを選択

#### 11.3 環境変数の設定

Railway ダッシュボードで以下の環境変数を設定:

- `YOUTUBE_API_KEY`
- `JWT_SECRET`
- `MYSQL_ROOT_PASSWORD`
- `NEXT_PUBLIC_API_URL`

#### 11.4 デプロイの確認

Railway が自動的にビルドとデプロイを実行します。

## トラブルシューティング

### MySQL 接続エラー

\`\`\`bash

# MySQL コンテナの状態を確認

docker ps | grep mysql

# ログを確認

docker logs <container-id>
\`\`\`

### Spring Boot 起動エラー

\`\`\`bash

# ログを確認

tail -f backend/logs/spring-boot.log

# ポートが使用中の場合

lsof -i :8080
kill -9 <PID>
\`\`\`

### Next.js ビルドエラー

\`\`\`bash

# node_modules を削除して再インストール

rm -rf frontend/node_modules
rm frontend/package-lock.json
cd frontend && npm install
\`\`\`

## 次のステップ

- [ ] カスタムドメインの設定
- [ ] SSL 証明書の設定
- [ ] モニタリングの設定（Prometheus, Grafana）
- [ ] ログ集約の設定（ELK Stack）
- [ ] バックアップの設定
