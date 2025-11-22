/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  output: 'standalone',
  experimental: {
    serverActions: {
      enabled: true,
    },
  },
  images: {
    domains: ['i.ytimg.com', 'yt3.ggpht.com'],
  },
};

module.exports = nextConfig;
