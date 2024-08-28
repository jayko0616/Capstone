# Recommender System 적용 방법

# 0. SSH로 EC2 접속
![ssh](./img/ssh.png)
EC2 인스턴스 생성시에 발급받았던 인증서와 user, host를 이용하여 SSH로 접속한다.
```commandline
ssh -i {certificate.pem} user@host
```
# 1. Docker 컨테이너 빌드 및 푸시

## 1.1 컨테이너 빌드
먼저 리포지토리에 있는 Dockerfilerhk Dockerfile.base를 로컬 환경에서 빌드한다[^1].  
이 때, Dockerfile들은 리포지토리 루트에 존재해야하고, 플랫폼 옵션을 배포환경에 맞춰서 바꿔줘야한다.

```commandline
docker build --platform linux/amd64 -f Dockerfile.base -t conda-base:latest .
docker build -t capstone:latest .
```
![build](./img/build.png)
성공적으로 빌드되면 도커 프로그램에서 위와 같이 확인할 수 있다.

## 1.2 컨테이너 푸시
빌드한 컨테이너를 도커 허브로 푸시하기 위해서는 먼저 태깅이 필요하다. 명령어는 아래와 같다.
```commandline
docker tag capstone:latest {username}/conda-base:latest
docker tag capstone:latest {username}/capstone:latest
```
그리고 태그에 맞춰 허브로 푸시를 진행한다.
```commandline
docker push {username}/conda-base:latest 
docker push {username}/capstone:latest
```
허브로 푸시가 정상적으로 완료되면 아래와 같이 확인할 수 있다.
![push](./img/push.png)
# 2. github action 등록
리포지토리 루트 디렉터리 아래에 `.github/workflows` 디렉터리를 생성하고 그 안에 
`deploy.yml` 파일을 생성한다.  
`deploy.yml` 파일의 내용은 아래와 같다. username 부분을 잊지말고 자신의 도커 username으로 변경해야한다.

```yaml
name: Deploy to EC2

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up Docker Buildx
        uses: docker/setup-buildx-action@v1

      - name: Log in to Docker Hub
        run: echo "${{ secrets.DOCKER_HUB_PASSWORD }}" | docker login -u ${{ secrets.DOCKER_HUB_USERNAME }} --password-stdin

      - name: Build and push Docker image
        run: |
          docker build --platform linux/amd64 -t capstone:latest .
          docker tag capstone:latest {username}/capstone:latest
          docker push {username}/capstone:latest

  deploy:
    needs: build
    runs-on: ubuntu-latest

    steps:
      - name: SSH and deploy
        uses: appleboy/ssh-action@v0.1.5
        with:
          host: ${{ secrets.EC2_HOST }}
          username: ${{ secrets.EC2_USER }}
          key: ${{ secrets.SSH_PRIVATE_KEY }}
          script: |
            docker pull {username}/capstone:latest
            docker stop capstone || true
            docker rm capstone || true
            docker run -d -p 5000:5000 --name capstone {username}/capstone:latest
```
리포지토리의 Setting에 들어가 보면 `Secrets and variables` 탭이 있다.  
여기에 아래 그림과 같이 5개의 비밀키를 등록한다.  
![private keys](./img/keys.png)
#### 각 키에 대한 간단 설명
- DOCKER_HUB_PASSWORD: Docker 비밀번호
- DOCKER_HUB_USERNAME: Docker 아이디
- EC2_HOST: EC2 인스턴스의 ip주소
- EC2_USER: EC2 인스턴스의 사용자명, 우분투 기본세팅이면 ubuntu (ssh 접속 시 @ 앞에 오는 문자)
- SSH_PRIVATE_KEY: 로컬에서 생성한 SSH 비밀키. 공개키는 미리 서버에 등록해둔다. 여기서는 다루지 않는다.

# 3. 코랩과 깃허브 연동
먼저 깃허브 토큰을 발급받아야 한다. Settings>Developer Settings로 들어가서 Personal access tokens의 Tokens(Classic)을 선택한 후 아래 그림과 같이 토큰을 만든다.
![token](./img/token.png)
권한 설정은 아래와 같이 진행한다..
![token setting](./img/tokenset.png)

코랩을 열고 새로운 노트북을 아래와 같이 등록한다.  

![colab](./img/colab.png)
- 여기서 두번째 `cd `명령어는 리포지토리를 클론할 위치로 선택한다.  
- 깃 클론 명령어 형식은`!git clone https://{username}:{public_access_token}@github.com/{organization}/{repository}.git` 이다.
- `git config`의 name과 email은 깃허브에 등록된 것으로 한다.
- 연결이 완료되면 `final_NeuMF.ipynb`을 코랩에서 실행하여 학습을 진행하면 된다.

# 4. 서버 세팅
먼저 EC2에 도커를 설치하고 본인의 계정으로 로그인을 해야한다.  
EC2에 SSH로 접속 후, 아래의 명령어들을 순차적으로 입력한다.
1. 필요한 패키지 설치
```commandline
sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common -y
```
2. Docker의 공식 GPG 키 추가
```commandline
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```
3. Docker 저장소 추가
```commandline
sudo add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable"
```
4. Docker 설치
```commandline
sudo apt-get update
sudo apt-get install docker-ce -y
```
5. Docker 서비스 시작 및 부팅 시 자동 시작 설정:
```commandline
sudo systemctl start docker
sudo systemctl enable docker
```
6. 현재 사용자에게 Docker 그룹 권한 부여
```commandline
sudo usermod -aG docker ${USER}
```
위의 작업이 끝나면 SSH를 재접속 해준다.
7. 도커 로그인
```commandline
docker login
```
---
이제 깃 클론을 진행한다.
```commandline
git clone git@github.com:{organization}/{repository}.git
```

만약 오류가 발생한다면 로컬에서 발급받은 SSH 퍼블릭 키를 EC2에 저장하고 비밀키는 깃허브의 Settings > SSH and GPG keys에서 New SSH key으로 등록한다.
이 방법으로 되지 않는다면 위에서 했던 것처럼 public access token을 사용해도 된다.

리포지토리가 정상적으로 클론되었다면 서버에서 학습이 이루어지면 추천시스템이 자동으로 업데이트 되는 파이프라인이 완성된다.

![workflow](./img/workflow.png)

# 5. 동작 확인
EC2 서버로 API 요청을 보내면 각 상황에 맞게 원하는 반환값을 반환함을 확인할 수 있다. 여기서는 threshold값을 10개로 설정했지만 목적에 따라 임의의 값으로 변경 가능하다.
- 유저의 평가가 10개 미만인 경우: 평점이 높은 순서대로 item_id 반환
![case1](./img/case1.png)
- 유저의 평가가 10개 이상인 경우: 개인별 추천 시스템 적용
![case2](./img/case2.png)
- 유저가 새롭게 만들어지거나 DB에 없는 user_id가 요청으로 들어온 경우: 평점이 높은 순서대로 item_id 반환
![case3](./img/case3.png)
[^1]: 학습하기 위해 매번 데이터가 갱신될 때 마다 가상환경을 새롭게 만들 필요가 없었으므로 도커 파일을 분리하여 빌드 시간을 단축하였다.

