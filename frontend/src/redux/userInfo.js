// src/redux/userdata.js

import { createSlice } from "@reduxjs/toolkit";

const initialState = {
};

export let userInfoSlice = createSlice({
	name: "userInfo", // slice 식별 이름 작명
	initialState,
	reducers: {
		setUserInfo: (state, action) => {
			return {
				...state,
				...action.payload,
			} || null;
			// 참고 ) 리덕스는 Immer 라이브러리 있어서
			// `...` 안써도 됨..!
		},
		logout: () => {
			return null;
		},
	},
});

//reducers에 작성한 함수는 변수명.actions 로 접근 가능하다.
export let { setUserInfo, logout } = userInfoSlice.actions;

export default userInfoSlice;
