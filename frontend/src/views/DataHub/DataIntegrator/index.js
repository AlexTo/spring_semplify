import React, {useState} from 'react';
import {makeStyles} from '@material-ui/styles';
import {Container, Tabs, Tab, Divider, colors} from '@material-ui/core';
import Page from 'src/components/Page';
import Header from "./Header";
import SubmitFilesDialog from "./SubmitFilesDialog";
import TaskTable from "./TaskTable";
import {useMutation, useQuery} from "@apollo/react-hooks";
import {FILE_ID, FILES_INTEGRATION, taskQueries} from "../../../queries/taskQueries";
import SearchBar from "../../../components/SearchBar";
import {useSelector} from "react-redux";
import FileTable from "./FileTable";

const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  },
  result: {
    marginTop: theme.spacing(3)
  },
  tabs: {
    marginTop: theme.spacing(3)
  },
  divider: {
    backgroundColor: colors.grey[300]
  },
}));

function DataIntegrator({match, history}) {
  const classes = useStyles();
  const tabs = [
    {value: 'tasks', label: 'Tasks'},
    {value: 'files', label: 'Files'},
    {value: 'urls', label: 'Urls'}];
  const {id, tab} = match.params;
  const taskReducer = useSelector(state => state.taskReducer);


  const handleTabsChange = (event, value) => {
    history.push(value);
  };

  const [submitFilesDialogOpen, setSubmitFilesDialogOpen] = useState(false);

  const [createTask, {}] = useMutation(taskQueries.createTask);

  const handleFilesSubmit = (files) => {
    setSubmitFilesDialogOpen(false);
    createTask({
      variables: {
        task: {
          type: FILES_INTEGRATION,
          parameters: files.map(f => {
            return {
              name: FILE_ID,
              value: f.id
            }
          })
        }
      }
    }).then(() => {

    });
  };

  return (
    <Page
      className={classes.root}
      title="Data Integrator">
      <Container maxWidth={false}>
        <Header onSubmitFiles={() => setSubmitFilesDialogOpen(true)}/>
        <SearchBar/>
        <Tabs
          className={classes.tabs}
          onChange={handleTabsChange}
          scrollButtons="auto"
          value={tab}
          variant="scrollable"
        >
          {tabs.map((tab) => (
            <Tab
              key={tab.value}
              label={tab.label}
              value={tab.value}
            />
          ))}
        </Tabs>
        <Divider className={classes.divider}/>

        {tab === 'tasks' && <TaskTable className={classes.result}/>}
        {tab === 'files' && <FileTable/>}

      </Container>
      <SubmitFilesDialog open={submitFilesDialogOpen}
                         onClose={() => setSubmitFilesDialogOpen(false)}
                         onCancel={() => setSubmitFilesDialogOpen(false)}
                         onSubmit={handleFilesSubmit}/>
    </Page>
  );
}

export default DataIntegrator;
