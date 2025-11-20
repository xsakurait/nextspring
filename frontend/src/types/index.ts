export interface User {
  id: number;
  username: string;
  email: string;
}

export interface Channel {
  id: number;
  channelId: string;
  title: string;
  description: string;
  thumbnailUrl: string;
  subscriberCount: number;
  videoCount: number;
  viewCount: number;
  customName?: string;
  category?: Category;
}

export interface Video {
  id: number;
  videoId: string;
  title: string;
  description: string;
  thumbnailUrl: string;
  publishedAt: string;
  duration: string;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  channel?: Channel;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  color: string;
  channelCount?: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  username: string;
  email: string;
}
