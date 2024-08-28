FROM nograd/conda-base:latest

# 작업 디렉토리 설정
WORKDIR /app

COPY . .

EXPOSE 5000

CMD ["conda", "run", "--no-capture-output", "-n", "capstone", "python", "app.py"]