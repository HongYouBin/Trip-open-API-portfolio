// src/redux/store.js
import { combineReducers } from "@reduxjs/toolkit";
import userInfoSlice from "./userInfo.js";

import storage from "redux-persist/lib/storage";
import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
import { persistReducer } from "redux-persist";
import { FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER } from "redux-persist";
import persistStore from "redux-persist/es/persistStore";

const persistConfig = {
	key: "root",
	storage,
};

const rootReducer = combineReducers({
  userInfo: userInfoSlice.reducer,
  // persist해야되는 리듀서들이 있다면 여기에 추가
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: {
    persistedReducer,
    // 다른 리듀서들도 추가된다면 여기에 추가
  },
	middleware: getDefaultMiddleware({
		serializableCheck: {
			ignoredActions: [FLUSH, REHYDRATE, PAUSE, PERSIST, PURGE, REGISTER],
		},
	}),
});

// persistor를 export 합니다.
export const persistor = persistStore(store);

export default store;
