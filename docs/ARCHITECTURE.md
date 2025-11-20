# プロジェクト構造

\`\`\`
nextspring/
├── .devcontainer/ # DevContainer 設定
│ ├── devcontainer.json # VS Code DevContainer 設定
│ ├── docker-compose.yml # 開発環境用 Docker Compose
│ ├── Dockerfile # DevContainer 用 Dockerfile
│ └── init.sql # MySQL 初期化スクリプト
│
├── .github/ # GitHub 設定
│ └── workflows/
│ └── ci-cd.yml # CI/CD パイプライン
│
├── .vscode/ # VS Code 設定
│ ├── settings.json # エディタ設定
│ └── java-formatter.xml # Java フォーマッター設定
│
├── argocd/ # ArgoCD 設定
│ └── application.yaml # ArgoCD Application 定義
│
├── backend/ # Spring Boot バックエンド
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/youtube/channelmanager/
│ │ │ │ ├── config/ # 設定クラス
│ │ │ │ │ └── SecurityConfig.java
│ │ │ │ ├── controller/ # REST API コントローラー
│ │ │ │ │ ├── AuthController.java
│ │ │ │ │ ├── ChannelController.java
│ │ │ │ │ ├── VideoController.java
│ │ │ │ │ └── CategoryController.java
│ │ │ │ ├── dto/ # データ転送オブジェクト
│ │ │ │ │ ├── VideoDTO.java
│ │ │ │ │ ├── ChannelDTO.java
│ │ │ │ │ ├── CategoryDTO.java
│ │ │ │ │ ├── LoginRequest.java
│ │ │ │ │ ├── SignupRequest.java
│ │ │ │ │ └── JwtResponse.java
│ │ │ │ ├── entity/ # JPA エンティティ
│ │ │ │ │ ├── User.java
│ │ │ │ │ ├── Role.java
│ │ │ │ │ ├── Category.java
│ │ │ │ │ ├── YoutubeChannel.java
│ │ │ │ │ ├── UserChannelSubscription.java
│ │ │ │ │ └── Video.java
│ │ │ │ ├── mapper/ # MyBatis Mapper
│ │ │ │ │ ├── VideoMapper.java
│ │ │ │ │ ├── UserChannelSubscriptionMapper.java
│ │ │ │ │ └── dto/ # MapStruct Mapper
│ │ │ │ │ ├── VideoDTOMapper.java
│ │ │ │ │ ├── ChannelDTOMapper.java
│ │ │ │ │ └── CategoryDTOMapper.java
│ │ │ │ ├── repository/ # Spring Data JPA Repository
│ │ │ │ │ ├── UserRepository.java
│ │ │ │ │ ├── RoleRepository.java
│ │ │ │ │ ├── CategoryRepository.java
│ │ │ │ │ ├── YoutubeChannelRepository.java
│ │ │ │ │ ├── VideoRepository.java
│ │ │ │ │ └── UserChannelSubscriptionRepository.java
│ │ │ │ ├── security/ # セキュリティ関連
│ │ │ │ │ ├── JwtTokenProvider.java
│ │ │ │ │ ├── JwtAuthenticationFilter.java
│ │ │ │ │ └── CustomUserDetailsService.java
│ │ │ │ ├── service/ # ビジネスロジック
│ │ │ │ │ ├── YouTubeService.java
│ │ │ │ │ ├── VideoService.java
│ │ │ │ │ ├── ChannelService.java
│ │ │ │ │ └── impl/
│ │ │ │ │ ├── YouTubeServiceImpl.java
│ │ │ │ │ ├── VideoServiceImpl.java
│ │ │ │ │ └── ChannelServiceImpl.java
│ │ │ │ └── YouTubeChannelManagerApplication.java
│ │ │ └── resources/
│ │ │ ├── application.properties
│ │ │ └── mapper/ # MyBatis XML マッパー
│ │ └── test/ # テストコード
│ ├── Dockerfile # 本番用 Dockerfile
│ ├── pom.xml # Maven 設定
│ └── .gitignore
│
├── frontend/ # Next.js フロントエンド
│ ├── src/
│ │ ├── app/ # App Router
│ │ │ ├── dashboard/
│ │ │ │ └── page.tsx # ダッシュボードページ
│ │ │ ├── login/
│ │ │ │ └── page.tsx # ログインページ
│ │ │ ├── layout.tsx # ルートレイアウト
│ │ │ ├── page.tsx # ホームページ
│ │ │ └── globals.css # グローバル CSS
│ │ ├── components/ # React コンポーネント
│ │ │ ├── VideoCard.tsx # 動画カード
│ │ │ └── ChannelCard.tsx # チャンネルカード
│ │ ├── lib/ # ユーティリティ
│ │ │ └── api.ts # API クライアント
│ │ ├── store/ # 状態管理
│ │ │ └── index.ts # Zustand ストア
│ │ └── types/ # TypeScript 型定義
│ │ └── index.ts
│ ├── public/ # 静的ファイル
│ ├── Dockerfile # 本番用 Dockerfile
│ ├── next.config.js # Next.js 設定
│ ├── tsconfig.json # TypeScript 設定
│ ├── tailwind.config.js # Tailwind CSS 設定
│ ├── .eslintrc.json # ESLint 設定
│ ├── .prettierrc # Prettier 設定
│ ├── package.json
│ └── .gitignore
│
├── k8s/ # Kubernetes マニフェスト
│ ├── mysql-deployment.yaml # MySQL Deployment
│ ├── backend-deployment.yaml # Spring Boot Deployment
│ ├── frontend-deployment.yaml # Next.js Deployment
│ └── ingress.yaml # Ingress 設定
│
├── docs/ # ドキュメント
│ └── SETUP.md # セットアップガイド
│
├── scripts/ # ユーティリティスクリプト
│ ├── start-dev.sh # 開発環境起動 (Linux/Mac)
│ └── start-dev.bat # 開発環境起動 (Windows)
│
├── .env.example # 環境変数テンプレート
├── .gitignore # Git 除外設定
└── README.md # プロジェクト概要
\`\`\`

## 主要ディレクトリの説明

### `/backend`

Spring Boot バックエンドアプリケーション。レイヤードアーキテクチャを採用:

- **Controller**: REST API エンドポイント
- **Service**: ビジネスロジック
- **Repository**: データアクセス層（JPA）
- **Mapper**: MyBatis マッパー（複雑なクエリ用）
- **Entity**: データベースエンティティ
- **DTO**: API 通信用データ転送オブジェクト
- **Security**: JWT 認証と Spring Security 設定

### `/frontend`

Next.js 14 フロントエンドアプリケーション。App Router を使用:

- **app**: ページとレイアウト（Server Components 対応）
- **components**: 再利用可能な React コンポーネント
- **lib**: API クライアントとユーティリティ関数
- **store**: Zustand 状態管理
- **types**: TypeScript 型定義

### `/k8s`

Kubernetes デプロイメント設定:

- MySQL、Spring Boot、Next.js の各 Deployment
- Service 定義
- Ingress 設定（HTTPS 対応）

### `/.devcontainer`

VS Code DevContainer 設定:

- 開発環境の完全な定義
- MySQL、Java、Node.js を含む統合環境
- VS Code 拡張機能の自動インストール

### `/.github/workflows`

GitHub Actions CI/CD:

- 自動ビルド
- 自動テスト
- Docker イメージのビルドとプッシュ
- Kubernetes マニフェストの自動更新

## 技術スタックの詳細

### バックエンド技術

- **Spring Boot 3.2**: メインフレームワーク
- **Spring Security**: JWT 認証
- **Spring Data JPA**: ORM
- **MyBatis**: 複雑なクエリ用
- **MapStruct**: DTO 変換
- **Lombok**: ボイラープレートコード削減
- **MySQL 8.0**: データベース
- **YouTube Data API v3**: YouTube 連携

### フロントエンド技術

- **Next.js 14**: React フレームワーク
- **React 18**: UI ライブラリ
- **TypeScript**: 型安全性
- **Tailwind CSS**: スタイリング
- **Zustand**: 状態管理
- **Axios**: HTTP クライアント
- **date-fns**: 日付処理

### インフラ技術

- **Docker**: コンテナ化
- **Kubernetes**: オーケストレーション
- **ArgoCD**: GitOps CD
- **GitHub Actions**: CI/CD
- **Railway**: ホスティング

## 開発フロー

1. **ローカル開発**: DevContainer で統合開発環境を使用
2. **コミット**: Git でバージョン管理
3. **プッシュ**: GitHub Actions が自動ビルド・テスト
4. **マージ**: main ブランチへのマージでデプロイ準備
5. **デプロイ**: ArgoCD が自動的に Kubernetes にデプロイ

## データフロー

\`\`\`
ユーザー → Next.js → Spring Boot → MySQL
↓
YouTube API
\`\`\`

1. ユーザーが Next.js フロントエンドにアクセス
2. フロントエンドが Spring Boot API を呼び出し
3. バックエンドが MySQL からデータ取得
4. 必要に応じて YouTube API から最新データを取得
5. データをフロントエンドに返却
6. フロントエンドが美しい UI で表示
