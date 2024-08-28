import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from './mainpage/header';
import Footer from './mainpage/footer';
import MyPage from "../src/sidebar/mypage";
import StorePage from "./sidebar/storepage";
import MainPage from "./mainpage/mainpage"; // MainPage 컴포넌트 임포트
import ReviewListPage from "./sidebar/reviewlistpage";
import FavoriteListPage from "./sidebar/favoritelistpage";
import WriteReviewPage from "./sidebar/writeReview";
import MobileMyPage from "../src/sidebarmobile/Mmypage";
import MobileReviewPage from "./sidebarmobile/Mreviewpage";
import MobileFavoritePage from "./sidebarmobile/Mfavoritepage";
import MobileReviewlistpage from "./sidebarmobile/Mreviewlistpage";
import MobileFavoritelistpage from "./sidebarmobile/Mfavoritelistpage";
import MobileWritereviewpage from "./sidebarmobile/MwriteReview";
import { useMediaQuery } from "react-responsive"
import RegisterPage from "./mainpage/register/registerpage";
import LoginPage from "./mainpage/login/loginpage";
import StoreReviews from "./sidebar/storereviews";
//거의최종


export const Mobile = ({ children }) => {
  const isMobile = useMediaQuery({
    query: "(max-width:768px)"
  });
  return <>{isMobile && children}</>
}

export const Pc = ({ children }) => {
  const isPc = useMediaQuery({
    query: "(min-width:769px)"
  });
  return <>{isPc && children}</>
}

function App() {
    return (
        <Router>
        <Pc>
            <div className="App">
                <Header />
                <Routes>
                    <Route path="/RegisterPage" element={<RegisterPage/>}/>
                    <Route path="/LoginPage" element={<LoginPage/>}/>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/mypage" element={<MyPage />} />
                    <Route path="/storepage/:id" element={<StorePage />} />
                    <Route path="/writeReview/:id" element={<WriteReviewPage />} />
                    <Route path="/reviewlistpage" element={<ReviewListPage />} />
                    <Route path="/favoritelistpage" element={<FavoriteListPage />} />
                    <Route path="/writeReview" element={<WriteReviewPage />} />
                    <Route path="/storereviews/:id" element={<StoreReviews />} />
                </Routes>
                {/*<Footer />*/}
            </div>
        </Pc>
        <Mobile>
            <div className="App">
                <Header />
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/mypage" element={<MobileMyPage />} />
                    <Route path="/favoritepage" element={<MobileFavoritePage />} />
                    <Route path="/reviewpage" element={<MobileReviewPage />} />
                    <Route path="/reviewlistpage" element={<MobileReviewlistpage />} />
                    <Route path="/favoritelistpage" element={<MobileFavoritelistpage />} />
                    <Route path="/writeReview" element={<MobileWritereviewpage />} />

                </Routes>
                <Footer />
            </div>
        </Mobile>

        </Router>
        

    );
}

export default App;
