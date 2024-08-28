import torch
import torch.nn as nn


class NeuMF(nn.Module):
    def __init__(self, user_num, item_num, factor_num, layers, dropout):
        super(NeuMF, self).__init__()
        self.user_embedding_mf = nn.Embedding(user_num, factor_num)
        self.item_embedding_mf = nn.Embedding(item_num, factor_num)
        self.user_embedding_mlp = nn.Embedding(user_num, factor_num * 2)
        self.item_embedding_mlp = nn.Embedding(item_num, factor_num * 2)

        MLP_modules = []
        input_size = factor_num * 4
        for layer_size in layers:
            MLP_modules.append(nn.Linear(input_size, layer_size))
            MLP_modules.append(nn.ReLU())
            MLP_modules.append(nn.Dropout(dropout))
            input_size = layer_size
        self.MLP_layers = nn.Sequential(*MLP_modules)
        self.predict_layer = nn.Linear(factor_num + layers[-1], 1)

    def forward(self, user, item):
        user_embedding_mf = self.user_embedding_mf(user)
        item_embedding_mf = self.item_embedding_mf(item)
        mf_vector = user_embedding_mf * item_embedding_mf

        user_embedding_mlp = self.user_embedding_mlp(user)
        item_embedding_mlp = self.item_embedding_mlp(item)
        mlp_vector = torch.cat([user_embedding_mlp, item_embedding_mlp], dim=-1)
        mlp_vector = self.MLP_layers(mlp_vector)

        vector = torch.cat([mf_vector, mlp_vector], dim=-1)
        prediction = self.predict_layer(vector)
        return prediction
