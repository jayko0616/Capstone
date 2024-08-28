import React, { useRef, useEffect, useState } from 'react';
import './map.css';
import axios from 'axios';

const lat = 37.58307340673131; // 여기에 위도 숫자를 넣어주세요
const lng = 127.06084182742293; // 여기에 경도 숫자를 넣어주세요
const API_BASE_URL = 'http://3.34.86.246:8082';


const markers = new Array([]);
const infowindows = new Array([]);

function Map() {
    const mapRef = useRef(null);
    const [stores, setStores] = useState([]);
    const [addArr, setAddArr] = useState([]);
    const [show1, setShow1] = useState(0);
    const [show2, setShow2] = useState(0);
    const [show3, setShow3] = useState(0);
    const [newPos, setNewPos] = useState();

    // 모든 가게 가져오기
    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`${API_BASE_URL}/api/store/`);
                setStores(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, []);

    // 주소->좌표로 변환후 DB에 업데이트
    useEffect(() => {
        const { naver } = window;

        if (stores.length > 0) {
            const fetchCoordinates = async () => {
                const updatedAddArr = await Promise.all(
                    stores.map(async (store) => {
                        try {
                            const response = await new Promise((resolve, reject) => {
                                naver.maps.Service.geocode({ query: store.storeCode }, (status, res) => {
                                    if (status === naver.maps.Service.Status.ERROR) {
                                        reject(new Error('Geocode error'));
                                    } else {
                                        resolve(res.v2.addresses[0]);
                                    }
                                });
                            });
                            return response;
                        } catch (error) {
                            console.error('Error geocoding address:', error);
                            return null;
                        }
                    })
                );

                setAddArr(updatedAddArr.filter(item => item));
            };

            fetchCoordinates();
        }
    }, [stores]);

    // 가게 좌표값 업데이트
    useEffect(() => {
        if (addArr.length > 0) {
            const updateCoordinates = async () => {
                const token = localStorage.getItem('authToken')?.replace(/^Bearer\s*/, '');
                if (!token) {
                    console.warn('No auth token found');
                    return;
                }

                await Promise.all(
                    addArr.map(async (item, index) => {
                        try {
                            await axios.put(
                                `${API_BASE_URL}/api/store/update`,
                                {
                                    id: stores[index].id,
                                    coordinateX: "" + item.x,
                                    coordinateY: "" + item.y
                                },
                                {
                                    headers: {
                                        Authorization: token,
                                        'Content-Type': 'multipart/form-data'
                                    }
                                }
                            );
                            //console.log('Response:', response.data);
                        } catch (error) {
                            console.error('Error updating coordinates:', error);
                        }
                    })
                );
            };

            updateCoordinates();
        }
    }, [addArr, stores]);

    // 맵 생성
    useEffect(() => {
        const { naver } = window;

        if (mapRef.current && naver && stores.length > 0) {
            const location = new naver.maps.LatLng(lat, lng);
            const map = new naver.maps.Map(mapRef.current, {
                center: location,
                zoom: 15,
                minZoom: 7,
                zoomControl: true,
                zoomControlOptions: {
                    position: naver.maps.Position.TOP_RIGHT
                }
            });

            stores.forEach((store, index) => {
                if (store.coordinateY && store.coordinateX) {
                    const marker = new naver.maps.Marker({
                        map: map,
                        title: store.storeName,
                        position: new naver.maps.LatLng(store.coordinateY, store.coordinateX),
                    });

                    const infowindow = new naver.maps.InfoWindow({
                        content: [
                            '<div style="padding: 10px; box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 16px 0px; border-radius: 15px;">',
                            `   <div style="font-weight: bold; margin-bottom: 2px;">${store.storeName}</div>`,
                            `   <div style="font-size: 13px;">좋아요❤️: ${store.storeHits}<div>`,
                            `   <div style="font-size: 13px;">평점⭐: ${store.rating}<div>`,
                            `   <div style="font-size: 13px;">빈자리: ${store.capacity}<div>`,
                            "</div>",
                        ].join(""),
                        maxWidth: 140,
                        backgroundColor: "white",
                        borderColor: "darkseagreen",
                        borderWidth: 1,
                        anchorSize: new naver.maps.Size(10, 10),
                        anchorSkew: true,
                        borderRadius: 15,
                        anchorColor: "#eee",
                        pixelOffset: new naver.maps.Point(20, -20)
                    });

                    markers.push(marker);
                    infowindows.push(infowindow);
                }
            });

            function getClickHandler(seq) {
                return function () {
                    const marker = markers[seq];
                    const infoWindow = infowindows[seq];

                    if (infoWindow.getMap()) {
                        infoWindow.close();
                    } else {
                        infoWindow.open(map, marker);
                    }
                };
            }

            markers.forEach((marker, i) => {
                naver.maps.Event.addListener(marker, 'click', getClickHandler(i));
            });
            const newMarker = new naver.maps.Marker({
                position: location,
                map: map,
                content: "현재 위치"
            });

            naver.maps.Event.addListener(map, 'click', function (e) {
                if (location !== e.coord) {
                    newMarker.setPosition(e.coord);
                    setNewPos(e.coord);
                }
                console.log("newPos:", newPos);
            }, [newPos]);

            
            if (show1) {
                new naver.maps.Circle({
                    map: map,
                    center: location,
                    strokeColor: 'black',
                    strokeStyle: 'shortdasdot',
                    radius: 500,
                    fillColor: 'black',
                    fillOpacity: 0.1
                });
            }
            if (show2) {
                new naver.maps.Circle({
                    map: map,
                    center: location,
                    strokeColor: 'black',
                    strokeStyle: 'shortdasdot',
                    radius: 1000,
                    fillColor: 'black',
                    fillOpacity: 0.1
                });
            }
            if (show3) {
                new naver.maps.Circle({
                    map: map,
                    center: location,
                    strokeColor: 'black',
                    strokeStyle: 'shortdasdot',
                    radius: 2000,
                    fillColor: 'black',
                    fillOpacity: 0.1
                });
            }
        }
    }, [stores, show1, show2, show3, newPos]);

    return (
        <div>
            <div ref={mapRef} className="map-container">
                <div className="radius-container">
                    <div className="radius-maker">
                        <label><input type="checkbox" onChange={() => { setShow1(!show1) }} /> 500m</label>
                        <label><input type="checkbox" onChange={() => { setShow2(!show2) }} /> 1km</label>
                        <label><input type="checkbox" onChange={() => { setShow3(!show3); }} /> 2km</label>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Map;