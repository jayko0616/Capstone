from flask import Flask, request, jsonify
from flask_cors import CORS
from NeuMF_model import NeuMF
import recom_item

app = Flask(__name__)
CORS(app)  # 모든 라우트에 대해 CORS 활성화

recom_system = recom_item.ItemRecommender()


@app.route("/recommend", methods=["GET"])
def recommend():
    user_id = request.args.get("user_id")
    if user_id is None:
        return jsonify({"message": "user_id is None"}), 400
    user_id = int(user_id)

    recoms = recom_system.get_recommendation(user_id)
    return jsonify(recoms)


if __name__ == "__main__":
    app.run("0.0.0.0", port=5000, debug=True)