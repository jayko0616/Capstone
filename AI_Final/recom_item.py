import torch
import pandas as pd

from NeuMF_model import NeuMF

# 설정값 정의
config = {
    "model_path": "./models/NeuMF_state_dict.pth",
    "data_path": "./data/augmented_data.csv",
    "data_orig_path": "./data/ratings.csv",
}

args = {
    "factor_num": 8,
    "layers": [64, 32, 16, 8],
    "dropout": 0,
    "top_k": 10,
    "gpu": "0",
}


class ItemRecommender:
    def __init__(self):
        self.device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
        self.top_k = args["top_k"]

        self.data = pd.read_csv(config["data_path"])
        self.data_orig = pd.read_csv(config["data_orig_path"], sep=";")
        self.all_items = self.data["item_id"].unique()
        # self.user_ratings = self.data.groupby("user_id").size()

        self.model, self.user_num, self.item_num = self.load_model(
            config["model_path"],
            args["factor_num"],
            args["layers"],
            args["dropout"],
        )

    # 모델 로드 함수
    def load_model(self, model_path, factor_num, layers, dropout):
        checkpoint = torch.load(model_path, map_location=self.device)
        user_num = checkpoint["user_num"]
        item_num = checkpoint["item_num"]

        model = NeuMF(user_num, item_num, factor_num, layers, dropout).to(self.device)
        model.load_state_dict(checkpoint["model_state_dict"])
        model.eval()
        return model, user_num, item_num

    def get_top_items(self):
        item_mean_rating = self.data_orig.groupby("item_id")["rating"].mean()
        top_k_items = item_mean_rating.nlargest(self.top_k).index.tolist()

        return top_k_items

    # 추천 함수
    def neumf_recommend(self, user_id):
        user = torch.tensor([user_id] * len(self.all_items), dtype=torch.long).to(
            self.device
        )
        items = torch.tensor(self.all_items, dtype=torch.long).to(self.device)

        with torch.no_grad():
            predictions = self.model(user, items).squeeze()

        top_k_indices = torch.topk(predictions, self.top_k).indices
        recommended_items = items[top_k_indices].cpu().numpy().tolist()

        return recommended_items

    def get_recommendation(self, user_id, threshold=10):
        user_item_rating = self.data[self.data["user_id"] == user_id]

        if (
                user_id not in self.data["user_id"].unique()
                or len(user_item_rating) < threshold
        ):
            return self.get_top_items()
        else:
            return self.neumf_recommend(user_id)
