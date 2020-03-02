import React, {Fragment, useEffect, useState} from "react";
import {Tab, Tabs, Divider, colors, TextField} from "@material-ui/core";
import {makeStyles} from "@material-ui/core/styles";
import FilesDropzone from "../../../../components/FilesDropzone";
import Grid from "@material-ui/core/Grid";
import {useForm} from "react-hook-form";

const useStyles = makeStyles(theme => ({
  divider: {
    backgroundColor: colors.grey[300]
  },
  content: {
    marginTop: theme.spacing(3)
  }
}));

function SelectSource({files, onFilesChanged, onUrlChanged, onDepthChanged, onSourceTypeChanged, display}) {

  const classes = useStyles();
  const {register, watch} = useForm();

  const [currentTab, setCurrentTab] = useState('files');

  const url = watch('url', null);
  const depth = watch('depth');

  const tabs = [
    {value: 'files', label: 'Files'},
    {value: 'urls', label: 'URLs'}
  ];

  useEffect(() => {
    onUrlChanged(url);
  }, [url]);

  useEffect(() => {
    onDepthChanged(depth);
  }, [depth]);

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
        {currentTab === 'urls' && <Grid container spacing={1}>
          <Grid item md={11}>
            <TextField fullWidth label="URL" name="url"
                       variant="outlined" inputRef={register}/>
          </Grid>
          <Grid item md={1}>
            <TextField fullWidth label="Depth" name="depth"
                       variant="outlined" type="number" defaultValue={3}
                       inputRef={register}/>
          </Grid>
        </Grid>}
      </div>
    </div>
  )
}

export default SelectSource;
