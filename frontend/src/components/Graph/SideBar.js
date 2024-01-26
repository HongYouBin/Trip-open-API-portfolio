import React, { useEffect, useState } from "react";
import styles from "./SideBar.module.css";
import { NavLink } from "react-router-dom";
import { useParams } from "react-router-dom";

const SubTabTitle = [
  { index: 101, subTitle: "accommodation" },
  { index: 201, subTitle: "foodName" },
  { index: 202, subTitle: "foodTypeMain" },
  { index: 203, subTitle: "foodTypeSub" },
  { index: 204, subTitle: "score_and_star" },
  { index: 301, subTitle: "AttractionName" },
  { index: 302, subTitle: "overview" },
];

const SideBar = ({ onSetId }) => {
  const { tab } = useParams();
  const [tabsData, setTabsData] = useState([]);
  const [selectedTab, setSelectedTab] = useState("숙소 데이터");
  const [selectedSub, setSelectedSub] = useState(101);

  const setId = (data) => {
    onSetId(data);
  };

  useEffect(() => {
    if (tab === "restaurant") handleTabClick("음식점 데이터");
    else if (tab === "wiki") handleTabClick("위키 데이터");
    else handleTabClick("숙소 데이터");
  }, [tab]);

  const handleTabClick = (tab) => {
    setSelectedTab(tab);
    if (tab === "음식점 데이터") handleSubClick(201);
    else if (tab === "위키 데이터") handleSubClick(301);
    else handleSubClick(101);
  };

  const handleSubClick = (api_num) => {
    setSelectedSub(api_num);
    setId(api_num);
  };

  useEffect(() => {
    const getApiDocsList = () => {
      const groupedTabs = [
        {
          title: "숙소 데이터",
          url: "/graphs/accommodation",
          subTabs: SubTabTitle.filter((item) => item.index < 200),
        },
        {
          title: "음식점 데이터",
          url: "/graphs/restaurant",
          subTabs: SubTabTitle.filter(
            (item) => item.index >= 201 && item.index < 300
          ),
        },
        {
          title: "위키 데이터",
          url: "/graphs/wiki",
          subTabs: SubTabTitle.filter(
            (item) => item.index >= 301 && item.index < 400
          ),
        },
      ];
      setTabsData(groupedTabs);
      // console.log(tabsData);
    };

    getApiDocsList();
  }, []);

  return (
    <div className={styles.sidebar}>
      <aside>
        <ul>
          {tabsData.map((group) => (
            <li key={group.title}>
              <h3
                className={
                  selectedTab === group.title ? styles.selectedTab : ""
                }
              >
                <NavLink
                  to={group.url}
                  onClick={() => handleTabClick(group.title)}
                >
                  <p
                    className={
                      selectedTab === group.title ? styles.selectedTabText : ""
                    }
                  >
                    {group.title}
                  </p>
                </NavLink>
              </h3>
              <ul>
                {selectedTab === group.title &&
                  group.subTabs.map((tab) => (
                    <li key={tab.index}>
                      <div
                        className={
                          selectedSub === tab.index
                            ? styles.selected
                            : styles.noSelected
                        }
                        onClick={() => handleSubClick(tab.index)}
                      >
                        {tab.subTitle}
                      </div>
                    </li>
                  ))}
              </ul>
            </li>
          ))}
        </ul>
      </aside>
    </div>
  );
};

export default SideBar;
