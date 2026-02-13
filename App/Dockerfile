FROM python:3.11-slim As builder
WORKDIR /builder
COPY ./FinalProject/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

FROM python:3.11-slim
WORKDIR /app/FinalProject
COPY --from=builder /usr/local /usr/local
COPY ./FinalProject /app/FinalProject
EXPOSE 5000
CMD [ "python","app.py" ]
