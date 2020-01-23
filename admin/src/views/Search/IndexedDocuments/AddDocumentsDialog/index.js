import React, {useEffect, useState} from "react";
import {
  Dialog,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  Slide,
  DialogContent,
  Step, StepLabel,
  Stepper,
  DialogActions,
  Button
} from "@material-ui/core";
import {makeStyles} from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import SelectSource from "./SelectSource";
import ItemProgressDialog from "./ItemProgressDialog";
import {useKeycloak} from "@react-keycloak/web";
import {nerService} from "../../../../services";
import AnnotationResults from "./AnnotationResults";

const useStyles = makeStyles(theme => ({
  appBar: {
    position: 'relative',
  },
  title: {
    marginLeft: theme.spacing(2),
    flex: 1,
    color: theme.palette.common.white
  },
  backButton: {
    marginRight: theme.spacing(1),
  },
}));

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

function AddDocumentsDialog({open, onClose}) {

  const classes = useStyles();
  const {keycloak} = useKeycloak();
  const [activeStep, setActiveStep] = useState(0);

  const [itemProgressDialogOpen, setItemProgressDialogOpen] = useState(false);
  const [currentItem, setCurrentItem] = useState(null);
  const [completed, setCompleted] = useState(0);

  const [files, setFiles] = useState([]);
  const [urls, setUrls] = useState([]);
  const [annotations, setAnnotations] = useState([]);
  const [sourceType, setSourceType] = useState('files');
  const [nextButtonEnabled, setNextButtonEnabled] = useState(false);

  const steps = [
    {value: 0, label: 'Select source'},
    {value: 1, label: 'Verify'},
    {value: 2, label: 'Submit'}
  ];

  useEffect(() => {
    if (sourceType === "files") {
      if (files.length === 0) {
        setNextButtonEnabled(false);
      } else {
        setNextButtonEnabled(true);
      }
    } else if (sourceType === "urls") {
      if (urls.length === 0) {
        setNextButtonEnabled(false);
      } else {
        setNextButtonEnabled(true);
      }
    }
  }, [sourceType, files, urls]);

  const handleFilesChanged = (files) => {
    setFiles(files);
  };

  const handleUrlsChanged = (urls) => {
    setUrls(urls);
  };

  const handleSourceTypeChanged = (type) => {
    setSourceType(type);
  };

  const handleUploadCancel = () => {
    setItemProgressDialogOpen(false);
  };

  const handleNext = async () => {
    if (activeStep === 0) {
      await analyse();
    } else if (activeStep === 1) {
      await verify();
    }
    setActiveStep(activeStep + 1);
  };

  const handleBack = () => {
    setActiveStep(activeStep - 1);
  };

  const analyse = async () => {
    setItemProgressDialogOpen(true);
    if (sourceType === "files") {
      const totalSize = files
        .map(f => f.size)
        .reduce((s1, s2) => s1 + s2, 0);
      let processedSize = 0.0;
      setCompleted(0);
      const results = [];
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        setCurrentItem(file.name);
        const result = await nerService.annotateFileAsync(file, keycloak.idToken);

        results.push({
          id: file.name,
          label: file.name,
          resources: result.body.resources
        });

        processedSize += file.size;
        setCompleted((processedSize / totalSize) * 100);
      }
      setAnnotations(results);
    } else {
    }
    setItemProgressDialogOpen(false);
  };

  const verify = async () => {

  };

  return (
    <div>
      <Dialog fullScreen open={open} TransitionComponent={Transition}>
        <AppBar className={classes.appBar}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={onClose} aria-label="close">
              <CloseIcon/>
            </IconButton>
            <Typography variant="h4" className={classes.title}>
              Add documents
            </Typography>
          </Toolbar>
        </AppBar>
        <DialogContent>
          <Stepper activeStep={activeStep}>
            {steps.map(step => (
              <Step key={step.value}>
                <StepLabel>{step.label}</StepLabel>
              </Step>
            ))}
          </Stepper>
          {activeStep === 0 && <SelectSource onFilesChanged={handleFilesChanged}
                                             onUrlsChanged={handleUrlsChanged}
                                             onSourceTypeChanged={handleSourceTypeChanged}/>}

          {activeStep === 1 && <AnnotationResults annotations={annotations}/>}
        </DialogContent>
        <DialogActions>
          <Button
            disabled={activeStep === 0}
            onClick={handleBack}
            className={classes.backButton}>
            Back
          </Button>
          {(activeStep === 0 || activeStep === 1) && <Button disabled={!nextButtonEnabled}
                                                             variant="contained"
                                                             color="primary"
                                                             onClick={handleNext}>
            Next
          </Button>}
        </DialogActions>
      </Dialog>
      <ItemProgressDialog open={itemProgressDialogOpen}
                          currentItem={currentItem}
                          completed={completed}
                          onCancel={handleUploadCancel}/>
    </div>
  )
}

export default AddDocumentsDialog;