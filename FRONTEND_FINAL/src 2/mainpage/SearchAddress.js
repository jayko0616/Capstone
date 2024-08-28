import React, { useRef, useEffect, useState } from 'react';

function SearchAddressToCoordinate(address) {
    const { naver } = window;
    const [result , setResult] = useState();
    naver.maps.Service.geocode({
        query: address
    }, function(status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
            return alert('Something Wrong!');
        }

        if (response.v2.meta.totalCount === 0) {
            return alert('totalCount' + response.v2.meta.totalCount);
        }

        setResult(response.v2.addresses[0]);
    });

    return result;
}
export default SearchAddressToCoordinate;