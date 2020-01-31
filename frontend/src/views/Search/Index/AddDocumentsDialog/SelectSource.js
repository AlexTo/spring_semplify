import React, {Fragment, useState} from "react";
import {Tab, Tabs, Divider, colors} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import FilesDropzone from "../../../../components/FilesDropzone";

const useStyles = makeStyles(theme => ({
  divider: {
    backgroundColor: colors.grey[300]
  },
  content: {
    marginTop: theme.spacing(3)
  }
}));

function SelectSource({files, onFilesChanged, onSourceTypeChanged, display}) {

  const classes = useStyles();

  const [currentTab, setCurrentTab] = useState('files');


  const tabs = [
    {value: 'files', label: 'Files'},
    {value: 'urls', label: 'URLs'}
  ];

  const handleTabsChange = (event, value) => {
    setCurrentTab(value);
    onSourceTypeChanged(value);
  };

  return (
    <div style={{display: display}}>
      <Tabs
        onChange={handleTabsChange}
        scrollButtons="auto"
        value={currentTab}
        variant="scrollable">
        {tabs.map((tab) => (
          <Tab
            key={tab.value}
            label={tab.label}
            value={tab.value}/>
        ))}

      </Tabs>
      <Divider className={classes.divider}/>
      <div className={classes.content}>
        {currentTab === 'files' && <FilesDropzone files={files} onFilesChanged={onFilesChanged}/>}
      </div>
    </div>
  )
}

export default SelectSource;
